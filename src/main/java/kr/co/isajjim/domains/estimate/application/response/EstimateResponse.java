package kr.co.isajjim.domains.estimate.application.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record EstimateResponse(
        @Schema(description = "명세서 ID", example = "1")
        Long estimateId
) {
    public static EstimateResponse from(Long id) {
        return new EstimateResponse(id);
    }
}
