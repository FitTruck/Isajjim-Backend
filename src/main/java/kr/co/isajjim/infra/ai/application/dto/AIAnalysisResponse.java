package kr.co.isajjim.infra.ai.application.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;
import lombok.Builder;

import java.util.List;

// AI -> Backend API 호출 시 SnakeCase 적용
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record AIAnalysisResponse(
        List<ImageResult> results
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ImageResult(
            Long imageId,
            List<FurnitureInfo> objects
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record FurnitureInfo(
            FurnitureLabel label,
            FurnitureType type,
            Double width,
            Double depth,
            Double height,
            Double volume,
            Double centerX,
            Double centerY
    ) {
    }
}