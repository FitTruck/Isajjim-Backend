package kr.co.isajjim.domains.estimate.application.mapper;

import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;

public class EstimateMapper {
    public static Estimate toEstimate(
    ) {
        return Estimate.builder()
                .build();
    }
}
