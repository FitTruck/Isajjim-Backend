package kr.co.isajjim.domains.estimate.persistence.entity;

import lombok.Getter;

@Getter
public enum ItemType {
    TRUCK_1_TON(ItemCategory.TRUCK, "1톤 트럭"),
    TRUCK_2_5_TON(ItemCategory.TRUCK, "2.5톤 트럭"),
    TRUCK_5_TON(ItemCategory.TRUCK, "5톤 트럭"),

    BOX_5(ItemCategory.BOX, "우체국 5호 박스"),
    DAN_PLASTIC_BOX_5(ItemCategory.BOX, "단프라 박스 5호"),
    ;

    private final ItemCategory category;
    private final String description;

    ItemType(ItemCategory category, String description) {
        this.category = category;
        this.description = description;
    }
}
