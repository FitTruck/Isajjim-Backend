package kr.co.isajjim.domains.estimate.application.usecase;

import kr.co.isajjim.domains.estimate.application.mapper.EstimateMapper;
import kr.co.isajjim.domains.estimate.application.request.EstimateRequest;
import kr.co.isajjim.domains.estimate.application.request.EstimateUpdateRequest;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.domain.service.EstimateService;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.image.application.response.ImageAnalysisDto;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.infra.ai.domain.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstimateUseCase {

    private final EstimateService estimateService;
    private final AIService aiService;

    @Transactional
    public Long createAndAnalyze(EstimateRequest request) {
        Long estimateId = estimateService.createEstimate(request);

        List<ImageAnalysisDto> imageDtos = estimateService.getImageDtos(estimateId);
        aiService.analyzeFurniture(estimateId, imageDtos);

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
}
