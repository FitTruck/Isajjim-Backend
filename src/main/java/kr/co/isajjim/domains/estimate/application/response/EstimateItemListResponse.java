package kr.co.isajjim.domains.estimate.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record EstimateItemListResponse(
        @Schema(description = "견적 목록")
        List<EstimateItemResponse> items
) {
    public static EstimateItemListResponse toEstimateItemListResponse(List<EstimateItemResponse> items) {
        return EstimateItemListResponse.builder()
                .items(items)
                .build();
    }
}
