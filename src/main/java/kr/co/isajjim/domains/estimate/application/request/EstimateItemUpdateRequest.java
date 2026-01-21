package kr.co.isajjim.domains.estimate.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record EstimateItemUpdateRequest(

        @Schema(
                description = "가구 ID",
                example = "1"
        )
        @NotNull
        Long furnitureId,

        @Schema(
                description = "가구 수량",
                example = "1"
        )
        @NotNull
        Integer quantity
) {
}
