package kr.co.isajjim.domains.furniture.application.mapper;

import kr.co.isajjim.domains.furniture.application.dto.FurnitureResponse;
import kr.co.isajjim.domains.furniture.persistence.entity.Furniture;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;

public class FurnitureMapper {
    public static Furniture toFurniture(
            int quantity,
            AIAnalysisResponse.FurnitureInfo info
    ) {
        return Furniture.builder()
                .label(info.label())
                .type(info.type())
                .width(info.width())
                .depth(info.depth())
                .height(info.height())
                .quantity(quantity)
                .centerX(info.centerX())
                .centerY(info.centerY())
                .build();
    }

    public static FurnitureResponse fromFurniture(
            Furniture furniture
    ) {
        return FurnitureResponse.builder()
                .furnitureId(furniture.getId())
                .label(furniture.getLabel())
                .type(furniture.getType())
                .quantity(furniture.getQuantity())
                .centerX(furniture.getCenterX())
                .centerY(furniture.getCenterY())
                .width(furniture.getWidth())
                .height(furniture.getHeight())
                .depth(furniture.getDepth())
                .build();
    }
}