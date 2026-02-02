package kr.co.isajjim.domains.estimate.application.usecase;

import kr.co.isajjim.domains.estimate.application.mapper.EstimateMapper;
import kr.co.isajjim.domains.estimate.application.request.*;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.domain.event.EstimateCreatedEvent;
import kr.co.isajjim.domains.estimate.domain.service.EstimateNotificationService;
import kr.co.isajjim.domains.estimate.domain.service.EstimateService;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.furniture.domain.service.FurnitureService;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
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

    @Transactional
    public Long createAndAnalyze(EstimateRequest request) {
        Long estimateId = estimateService.createEstimate(request);

        List<ImageAnalysisDto> imageDtos = estimateService.getImageDtos(estimateId);
        eventPublisher.publishEvent(new EstimateCreatedEvent(estimateId, imageDtos));

        return estimateId;
    }

    public EstimateDetailResponse getDetailEstimates(Long estimateId) {
        Estimate estimate = estimateService.getEstimateById(estimateId);
        return EstimateMapper.fromEstimate(estimate);
    }

    @Transactional
    public void updateDefaultInfo(Long estimateId, EstimateUpdateRequest request) {
        Estimate estimate = estimateService.getEstimateById(estimateId);
        estimateService.updateDefaultInfo(estimate, request);
    }

    @Transactional
    public void updateFurniture(Long estimateId, EstimateFurnitureUpdateRequest request) {
        furnitureService.updateFurnitureQuantity(estimateId, request.furnitureId(), request.quantity());
    }

    @Transactional
    public void updateItems(Long estimateId, EstimateItemUpdateRequest request) {
        estimateService.updateItems(estimateId, request);
    }

    public SseEmitter subscribe(Long estimateId) {
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
}
