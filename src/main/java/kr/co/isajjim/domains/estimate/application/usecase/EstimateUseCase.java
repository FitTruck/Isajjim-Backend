package kr.co.isajjim.domains.estimate.application.usecase;

import kr.co.isajjim.domains.estimate.application.mapper.EstimateChatSummaryMapper;
import kr.co.isajjim.domains.estimate.application.mapper.EstimateMapper;
import kr.co.isajjim.domains.estimate.application.request.*;
import kr.co.isajjim.domains.estimate.application.response.EstimateChatSummaryResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.domain.event.EstimateCreatedEvent;
import kr.co.isajjim.domains.estimate.domain.service.EstimateNotificationService;
import kr.co.isajjim.domains.estimate.domain.service.EstimateService;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.furniture.domain.service.FurnitureService;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstimateUseCase {

    private final FurnitureService furnitureService;
    private final EstimateService estimateService;
    private final EstimateNotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;

    @Transactional
    public Long createAndAnalyze(EstimateRequest request, Long userId) {
        UserEntity user = userService.getUserById(userId);
        Long estimateId = estimateService.createEstimate(request, user);

        List<ImageAnalysisDto> imageDtos = estimateService.getImageDtos(estimateId);
        eventPublisher.publishEvent(new EstimateCreatedEvent(estimateId, imageDtos));

        return estimateId;
    }

    public EstimateDetailResponse getDetailEstimates(Long estimateId, Long userId) {
        Estimate estimate = estimateService.getEstimateById(estimateId);
        estimate.validateOwner(userId);

        return EstimateMapper.fromEstimate(estimate);
    }

    @Transactional
    public void updateDefaultInfo(Long estimateId, EstimateUpdateRequest request, Long userId) {
        Estimate estimate = estimateService.getEstimateById(estimateId);
        estimate.validateOwner(userId);

        estimateService.updateDefaultInfo(estimate, request);
    }

    @Transactional
    public void updateFurniture(Long estimateId, EstimateFurnitureUpdateRequest request, Long userId) {
        Estimate estimate = estimateService.getEstimateById(estimateId);
        estimate.validateOwner(userId);

        furnitureService.updateFurnitureQuantity(estimateId, request.furnitureId(), request.quantity());
    }

    @Transactional
    public void updateItems(Long estimateId, EstimateItemUpdateRequest request, Long userId) {
        Estimate estimate = estimateService.getEstimateById(estimateId);
        estimate.validateOwner(userId);

        estimateService.updateItems(estimate, request);
    }

    public SseEmitter subscribe(Long estimateId, Long userId) {
        Estimate estimate = estimateService.getEstimateById(estimateId);
        estimate.validateOwner(userId);

        SseEmitter emitter = notificationService.createConnection(estimateId);

        // AI 처리가 완료된 경우 즉시 알림 전송
        if (estimateService.isAIProcessingCompleted(estimateId)) {
            notificationService.sendNotify(estimateId);
        }

        return emitter;
    }

    @Transactional
    public void saveFurnitureList(Long estimateId, AIAnalysisResponse request) {
        furnitureService.saveFurnitureList(request.results());
        estimateService.updateAIStatus(estimateId, AIStatus.COMPLETED);
        notificationService.sendNotify(estimateId);
    }

    public EstimateChatSummaryResponse generateChatSummary(String chatContent) {
        String summary = estimateService.generateChatSummary(chatContent);
        return EstimateChatSummaryMapper.toEstimateChatSummaryResponse(summary);
    }
}
