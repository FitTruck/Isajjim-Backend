package kr.co.isajjim.domains.estimate.persistence.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
public enum ItemType {
    TRUCK_1_TON(ItemCategory.TRUCK, 6.0, "1톤 트럭"),
    TRUCK_1_4_TON(ItemCategory.TRUCK, 8.0, "1.4톤 트럭"),
    TRUCK_2_5_TON(ItemCategory.TRUCK, 14.0, "2.5톤 트럭"),
    TRUCK_3_5_TON(ItemCategory.TRUCK, 17.0, "3.5톤 트럭"),
    TRUCK_5_TON(ItemCategory.TRUCK, 30.0, "5톤 트럭"),

    BOX_5(ItemCategory.BOX, 0.07, "우체국 5호 박스"),
    DAN_PLASTIC_BOX_5(ItemCategory.BOX, 0.096, "단프라 박스 5호"),
    ;

    private final ItemCategory category;
    private final double capacity;
    private final String description;

    ItemType(ItemCategory category, double capacity, String description) {
        this.category = category;
        this.capacity = capacity;
        this.description = description;
    }

    // 트럭 타입만 필터링하여 용량순으로 반환
    public static List<ItemType> getTruckTypesAsc() {
        return Arrays.stream(values())
                .filter(type -> type.category == ItemCategory.TRUCK)
                .sorted(Comparator.comparingDouble(ItemType::getCapacity))
                .toList();
    }
}
