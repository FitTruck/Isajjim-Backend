package kr.co.isajjim.domains.estimate.application.usecase;

import kr.co.isajjim.domains.estimate.application.mapper.EstimateMapper;
import kr.co.isajjim.domains.estimate.application.request.EstimateItemUpdateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateUpdateRequest;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateItemListResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateItemResponse;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.domain.event.EstimateCreatedEvent;
import kr.co.isajjim.domains.estimate.domain.service.EstimateNotificationService;
import kr.co.isajjim.domains.estimate.domain.service.EstimateService;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.estimate.persistence.entity.EstimateItem;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;
import kr.co.isajjim.domains.furniture.domain.service.FurnitureService;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
import kr.co.isajjim.domains.image.persistence.entity.Image;
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

    public EstimateItemListResponse getItemList(Long estimateId) {
        Estimate estimate = estimateService.getEstimateById(estimateId);

        List<EstimateItem> items = estimate.getEstimateItems();
        List<EstimateItemResponse> list = items.stream().map(EstimateMapper::fromEstimateItem).toList();
        return EstimateMapper.toEstimateItemListResponse(list);
    }

    @Transactional
    public void updateDefaultInfo(Long estimateId, EstimateUpdateRequest request) {
        Estimate estimate = estimateService.getEstimateById(estimateId);
        estimateService.updateDefaultInfo(estimate, request);

        // 프론트 임시 Mock 데이터 반환을 위한 코드
        List<Long> list = estimate.getImages().stream().map(Image::getId).toList();
        AIAnalysisResponse response = getMockData(list);

        furnitureService.saveFurnitureList(response.results());
        estimateService.updateAIStatus(estimateId, AIStatus.COMPLETED);
    }

    @Transactional
    public void updateFurniture(Long estimateId, EstimateItemUpdateRequest request) {
        furnitureService.updateFurnitureQuantity(estimateId, request.furnitureId(), request.quantity());
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

    private AIAnalysisResponse getMockData(List<Long> imageIds) {
        List<AIAnalysisResponse.ImageResult> list = imageIds.stream().map(id -> new AIAnalysisResponse.ImageResult(
                id,
                List.of(
                        new AIAnalysisResponse.FurnitureInfo(FurnitureLabel.BED, 30.5, 20.0, 15.2, 1.0),
                        new AIAnalysisResponse.FurnitureInfo(FurnitureLabel.TV, 210.0, 90.0, 85.0, 1.0)
                )
        )).toList();

        return new AIAnalysisResponse(
                list
        );
    }
}
