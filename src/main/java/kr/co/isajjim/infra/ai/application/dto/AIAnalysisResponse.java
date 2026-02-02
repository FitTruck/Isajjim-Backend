package kr.co.isajjim.infra.ai.application.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;

import java.util.List;

// AI -> Backend API 호출 시 SnakeCase 적용
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AIAnalysisResponse(
        List<ImageResult> results
) {
    public record ImageResult(
            Long imageId,
            List<FurnitureInfo> objects
    ) {
    }

    public record FurnitureInfo(
            FurnitureLabel label,
            FurnitureType type,
            Double width,
            Double depth,
            Double height,
            Double volume,
            String plyUrl
    ) {
    }
}