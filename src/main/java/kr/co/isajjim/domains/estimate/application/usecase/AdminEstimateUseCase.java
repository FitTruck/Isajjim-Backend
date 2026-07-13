package kr.co.isajjim.domains.estimate.application.usecase;

import kr.co.isajjim.domains.estimate.application.mapper.EstimateMapper;
import kr.co.isajjim.domains.estimate.application.response.AdminEstimateResponse;
import kr.co.isajjim.domains.estimate.application.response.EstimateDetailResponse;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.estimate.domain.service.EstimateService;
import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEstimateUseCase {

    private final EstimateService estimateService;

    public Page<AdminEstimateResponse> getList(AIStatus status, String keyword, Pageable pageable) {
        return estimateService.getList(status, keyword, pageable)
                .map(EstimateMapper::fromEstimateForAdmin);
    }

    public EstimateDetailResponse getDetail(Long estimateId) {
        Estimate estimate = estimateService.getEstimateById(estimateId);
        return EstimateMapper.fromEstimate(estimate);
    }
}
