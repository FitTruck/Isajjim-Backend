package kr.co.isajjim.domains.furniture.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum FurnitureLabel {
    AIR_CONDITIONER(List.of(FurnitureType.WALL_MOUNTED_AIR_CONDITIONER, FurnitureType.STANDING_AIR_CONDITIONER)),
    COFFEE_TABLE(List.of(FurnitureType.DEFAULT_COFFEE_TABLE)),
    MIRROR(List.of(FurnitureType.DEFAULT_MIRROR)),
    STORAGE_BOX(List.of(FurnitureType.DEFAULT_STORAGE_BOX)),
    FAN(List.of(FurnitureType.DEFAULT_FAN)),
    CABINET(List.of(FurnitureType.DEFAULT_CABINET)),
    DRAWER(List.of(FurnitureType.DEFAULT_DRAWER)),
    NIGHTSTAND(List.of(FurnitureType.DEFAULT_NIGHTSTAND)),
    BOOKSHELF(List.of(FurnitureType.DEFAULT_BOOKSHELF)),
    DISPLAY_SHELF(List.of(FurnitureType.DEFAULT_DISPLAY_SHELF)),
    REFRIGERATOR(List.of(FurnitureType.TOP_BOTTOM_REFRIGERATOR, FurnitureType.SIDE_BY_SIDE_REFRIGERATOR, FurnitureType.FOUR_DOOR_REFRIGERATOR)),
    WARDROBE(List.of(FurnitureType.MOVABLE_WARDROBE, FurnitureType.SYSTEM_HANGER)),
    SOFA(List.of(FurnitureType.SINGLE_SOFA, FurnitureType.TWIN_SOFA, FurnitureType.THREE_SEATER_SOFA, FurnitureType.L_SHAPED_SOFA)),
    BED(List.of(FurnitureType.SINGLE_BED, FurnitureType.SUPER_SINGLE_BED, FurnitureType.DOUBLE_BED, FurnitureType.QUEEN_SIZE_BED, FurnitureType.KING_SIZE_BED, FurnitureType.BUNK_BED)),
    DINING_TABLE(List.of(FurnitureType.DEFAULT_DINING_TABLE)),
    MONITOR_TV(List.of(FurnitureType.DEFAULT_MONITOR_TV)),
    DESK(List.of(FurnitureType.L_SHAPED_DESK, FurnitureType.DESK_NO_DRAWER, FurnitureType.DESK_SINGLE_PEDESTAL, FurnitureType.DESK_DOUBLE_PEDESTAL)),
    CHAIR_STOOL(List.of(FurnitureType.STANDARD_CHAIR, FurnitureType.ROUND_STOOL, FurnitureType.ROLLING_OFFICE_CHAIR)),
    WASHING_MACHINE(List.of(FurnitureType.DRUM_WASHING_MACHINE, FurnitureType.TOP_LOADING_WASHING_MACHINE)),
    DRYER(List.of(FurnitureType.DEFAULT_DRYER)),
    VANITY_TABLE(List.of(FurnitureType.VANITY_TABLE_WITH_ATTACHED_MIRROR, FurnitureType.CONSOLE_VANITY_TABLE)),
    TV_STAND(List.of(FurnitureType.TV_ENTERTAINMENT_CENTER_WITH_STORAGE, FurnitureType.LOW_TV_STAND)),
    PIANO(List.of(FurnitureType.UPRIGHT_VERTICAL_PIANO, FurnitureType.GRAND_PIANO, FurnitureType.DIGITAL_PIANO)),
    MASSAGE_CHAIR(List.of(FurnitureType.DEFAULT_MASSAGE_CHAIR)),
    TREADMILL(List.of(FurnitureType.DEFAULT_TREADMILL)),
    EXERCISE_BIKE(List.of(FurnitureType.DEFAULT_EXERCISE_BIKE)),
    MICROWAVE_OVEN(List.of(FurnitureType.DEFAULT_MICROWAVE_OVEN)),
    DISH_CABINET(List.of(FurnitureType.DEFAULT_DISH_CABINET)),
    ;

    private final List<FurnitureType> subTypes;
}