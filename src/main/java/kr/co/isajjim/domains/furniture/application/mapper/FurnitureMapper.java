package kr.co.isajjim.domains.furniture.application.mapper;

import kr.co.isajjim.domains.furniture.FurnitureResponse;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;
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
                .width(info.dimensions().width())
                .depth(info.dimensions().depth())
                .height(info.dimensions().height())
                .volume(info.volume())
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
                .quantity(furniture.getQuantity())
                .build();
    }
}