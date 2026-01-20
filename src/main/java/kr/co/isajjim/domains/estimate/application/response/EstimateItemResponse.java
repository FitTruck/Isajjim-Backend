package kr.co.isajjim.domains.estimate.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.estimate.persistence.entity.ItemCategory;
import kr.co.isajjim.domains.estimate.persistence.entity.ItemType;
import lombok.Builder;

@Builder
public record EstimateItemResponse(
        @Schema(description = "견적 종류 (TRUCK/BOX)", example = "TRUCK")
        ItemCategory category,

        @Schema(description = "견적 상세", example = "TRUCK_1_TON")
        ItemType itemType,

        @Schema(description = "수량", example = "1")
        Integer quantity
) {
}
