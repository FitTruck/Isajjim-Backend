package kr.co.isajjim.domains.image.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.furniture.application.dto.FurnitureResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record ImageResponse(

        @Schema(description = "이미지 ID", example = "1")
        Long imageId,

        @Schema(description = "이미지 URL", example = "https://example.com/image1.jpg")
        String imageUrl,

        List<FurnitureResponse> furnitureList
) {
}
