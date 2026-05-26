package kr.co.isajjim.domains.estimate.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record EstimateDetailListResponse(
        @Schema(description = "견적서 목록")
        List<EstimateDetailResponse> items
) {
    public static EstimateDetailListResponse toEstimateDetailListResponse(List<EstimateDetailResponse> items) {
        return EstimateDetailListResponse.builder()
                .items(items)
                .build();
    }
}
