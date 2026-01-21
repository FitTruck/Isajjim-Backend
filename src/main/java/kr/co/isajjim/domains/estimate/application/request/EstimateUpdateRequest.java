package kr.co.isajjim.domains.estimate.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.co.isajjim.domains.estimate.domain.constant.*;

public record EstimateUpdateRequest(

        @Schema(
                description = "건물 종류",
                example = "APARTMENT"
        )
        @NotNull
        BuildingType buildingType,

        @Schema(
                description = "집 평수",
                example = "UNDER_10"
        )
        @NotNull
        RoomSize roomSize,

        @Schema(
                description = "층",
                example = "FL_1"
        )
        @NotNull
        Floor floor,

        @Schema(
                description = "엘리베이터",
                example = "true"
        )
        @NotNull
        Boolean elevator,

        @Schema(
                description = "사다리차",
                example = "REQUIRED"
        )
        @NotNull
        LadderTruck ladderTruck,

        @Schema(
                description = "방 구조",
                example = "STUDIO"
        )
        @NotNull
        RoomType roomType,

        @Schema(
                description = "복층",
                example = "false"
        )
        @NotNull
        Boolean duplex,

        @Schema(
                description = "1층 별도 계단",
                example = "false"
        )
        @NotNull
        Boolean groundStair,

        @Schema(
                description = "주차",
                example = "true"
        )
        @NotNull
        Boolean parking
) {
}
