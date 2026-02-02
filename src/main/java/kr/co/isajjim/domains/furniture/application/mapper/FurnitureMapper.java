package kr.co.isajjim.domains.furniture.application.mapper;

import kr.co.isajjim.domains.furniture.FurnitureResponse;
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
                .volume(info.volume())
                .plyUrl(info.plyUrl())
                .quantity(quantity)
                .build();
    }

    public static FurnitureResponse fromFurniture(
            Furniture furniture
    ) {
        return FurnitureResponse.builder()
                .furnitureId(furniture.getId())
                .label(furniture.getLabel())
                .type(furniture.getType())
                .plyUrl(furniture.getPlyUrl())
                .quantity(furniture.getQuantity())
                .build();
    }
}