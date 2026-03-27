package kr.co.isajjim.domains.furniture.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;
import lombok.Builder;

@Builder
public record FurnitureResponse(

        @Schema(description = "가구 ID", example = "1")
        Long furnitureId,

        @Schema(description = "가구 종류", example = "BED")
        FurnitureLabel label,

        @Schema(description = "가구 상세(Nullable)", example = "퀸사이즈")
        FurnitureType type,

        @Schema(description = "Ply File Url", example = "https://storage.isajjim.kr/123c")
        String plyUrl,

        @Schema(description = "개수", example = "1")
        Integer quantity,

        @Schema(description = "이미지 내 가구 X 위치", example = "43.2")
        Double centerX,

        @Schema(description = "이미지 내 가구 Y 위치", example = "234.2")
        Double centerY
) {
}
