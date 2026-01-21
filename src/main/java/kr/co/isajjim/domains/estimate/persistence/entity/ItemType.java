package kr.co.isajjim.domains.estimate.persistence.entity;

public enum ItemType {
    TRUCK_1_TON(ItemCategory.TRUCK),
    TRUCK_2_5_TON(ItemCategory.TRUCK),
    TRUCK_5_TON(ItemCategory.TRUCK),

    BOX_1(ItemCategory.BOX),
    BOX_2(ItemCategory.BOX),
    BOX_3(ItemCategory.BOX),
    BOX_4(ItemCategory.BOX),
    BOX_5(ItemCategory.BOX),
    BOX_6(ItemCategory.BOX)
    ;

    private final ItemCategory category;

    ItemType(ItemCategory category) {
        this.category = category;
    }
}
