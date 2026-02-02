package kr.co.isajjim.domains.estimate.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EstimateFurnitureUpdateRequest(

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
        @Min(0)
        @Max(100)
        Integer quantity
) {
}
