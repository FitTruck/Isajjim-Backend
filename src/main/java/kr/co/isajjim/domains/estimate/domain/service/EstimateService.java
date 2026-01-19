package kr.co.isajjim.domains.estimate.domain.service;

import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.estimate.persistence.repository.EstimateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstimateService {

    private final EstimateRepository estimateRepository;

    public Long createEstimate(Estimate estimate) {
        return estimateRepository.save(estimate).getId();
    }
}
