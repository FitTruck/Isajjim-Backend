package kr.co.isajjim.domains.estimate.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.estimate.persistence.entity.ItemCategory;
import kr.co.isajjim.domains.estimate.persistence.entity.ItemType;
import lombok.Builder;

import java.util.List;

@Builder
public record EstimateItemUpdateRequest(
        @Schema(description = "견적 목록")
        List<ItemDetail> items
) {
    public record ItemDetail(
            ItemCategory category,
            ItemType type,
            @Schema(example = "1")
            Integer quantity
    ) {
    }
}
