package kr.co.isajjim.infra.ai.application.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;

import java.util.List;

// AI -> Backend API 호출 시 SnakeCase 적용
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AIAnalysisResponse(
        List<ImageResult> itemsByImage,
        SimulationResult simulation
) {
    public record ImageResult(
            Long imageId,
            List<FurnitureInfo> objects
    ) {
    }

    public record FurnitureInfo(
            Long id,
            FurnitureLabel label,
            FurnitureType type,
            Dimensions dimensions,
            Double volume,
            String plyUrl
    ) {
    }

    public record Dimensions(
            Double width,
            Double depth,
            Double height
    ) {
    }

    public record SimulationResult(
            SimulationSummary summary,
            List<TruckResult> trucks,
            List<UnplacedItem> unplacedItems
    ) {
    }

    public record SimulationSummary(
            Integer totalTrucks,
            Integer totalItems,
            Double totalVolumeM3
    ) {
    }

    public record TruckResult(
            Integer truckIndex,
            String type,
            TruckSpec spec,
            Double utilization,
            List<PlacedItem> placedItems
    ) {
    }

    public record TruckSpec(
            String name,
            Dimensions dimensions,
            Integer maxWeight
    ) {}

    public record PlacedItem(
            Long itemId,
            Position position,
            Integer orientation,
            Integer order
    ) {}

    public record Position(
            Double x,
            Double y,
            Double z
    ) {}

    public record UnplacedItem(
            Long itemId,
            String reason
    ) {}

}