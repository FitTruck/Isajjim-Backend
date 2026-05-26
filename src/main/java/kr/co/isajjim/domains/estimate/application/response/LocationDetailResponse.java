package kr.co.isajjim.domains.estimate.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.estimate.domain.constant.*;

public record LocationDetailResponse(
        @Schema(description = "주소", example = "강원 춘천시 강원대학길 1")
        String address,

        @Schema(description = "상세 주소", example = "새롬관 남자동 909호")
        String detailAddress,

        @Schema(description = "건물 유형", example = "APARTMENT")
        BuildingType buildingType,

        @Schema(description = "방 크기", example = "BETWEEN_10_15")
        RoomSize roomSize,

        @Schema(description = "층 수", example = "FL_3")
        Floor floor,

        @Schema(description = "엘리베이터 유무", example = "true")
        Boolean elevator,

        @Schema(description = "사다리차 필요 여부", example = "REQUIRED")
        LadderTruck ladderTruck,

        @Schema(description = "방 유형", example = "STUDIO")
        RoomType roomType,

        @Schema(description = "복층 여부", example = "true")
        Boolean duplex,

        @Schema(description = "1층 계단 여부", example = "true")
        Boolean groundStair,

        @Schema(description = "주차 가능 여부", example = "true")
        Boolean parking
) {
}
