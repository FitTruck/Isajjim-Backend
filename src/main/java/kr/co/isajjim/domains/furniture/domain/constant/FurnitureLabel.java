package kr.co.isajjim.domains.furniture.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FurnitureLabel {
    AIR_CONDITIONER("에어컨"),
    COFFEE_TABLE("커피테이블"),
    MICROWAVE("전자레인지"),
    OVEN("오븐"),
    MIRROR("거울"),
    STORAGE_BOX("박스/수납함"),
    BENCH("벤치"),
    TOILET("변기"),
    SINK("싱크대"),
    BATHTUB("욕조"),
    BICYCLE("자전거"),
    LADDER("사다리"),
    FAN("선풍기"),
    BOX("박스"),
    CABINET("캐비닛"),
    KITCHEN_CABINET("찬장"),
    DRAWER("서랍장"),
    NIGHTSTAND("협탁"),
    BOOKSHELF("책장"),
    DISPLAY_SHELF("전시대/선반"),
    REFRIGERATOR("냉장고"),
    WARDROBE("장롱/수납장"),
    SOFA("소파"),
    BED("침대"),
    DINING_TABLE("식탁"),
    MONITOR_TV("모니터/TV"),
    DESK("책상"),
    CHAIR_STOOL("의자/스툴"),
    WASHING_MACHINE("세탁기"),
    DRYER("건조기"),
    POTTED_PLANT("화분/식물"),
    KIMCHI_REFRIGERATOR("김치냉장고"),
    VANITY_TABLE("화장대"),
    TV_STAND("TV 거치대"),
    PIANO("피아노"),
    MASSAGE_CHAIR("안마의자"),
    TREADMILL("러닝머신"),
    EXERCISE_BIKE("실내자전거"),
    ;

    private final String koreanLabel;
}
