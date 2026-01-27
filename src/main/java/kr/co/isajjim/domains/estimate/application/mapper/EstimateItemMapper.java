package kr.co.isajjim.domains.estimate.application.mapper;

import kr.co.isajjim.domains.estimate.persistence.entity.Estimate;
import kr.co.isajjim.domains.estimate.persistence.entity.EstimateItem;
import kr.co.isajjim.domains.estimate.persistence.entity.ItemCategory;
import kr.co.isajjim.domains.estimate.persistence.entity.ItemType;

public class EstimateItemMapper {
    public static EstimateItem toEstimateItem(
            Estimate estimate,
            ItemCategory category,
            ItemType type,
            Integer quantity
    ) {
        return EstimateItem.builder()
                .estimate(estimate)
                .category(category)
                .type(type)
                .quantity(quantity)
                .build();
    }
}
