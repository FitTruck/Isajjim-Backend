package kr.co.isajjim.domains.furniture.application.mapper;

import kr.co.isajjim.domains.furniture.FurnitureResponse;
import kr.co.isajjim.domains.furniture.persistence.entity.Furniture;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;

public class FurnitureMapper {
    public static Furniture toFurniture(
            AIAnalysisResponse.FurnitureInfo info
    ) {
        return Furniture.builder()
                .label(info.label())
                .width(info.width())
                .depth(info.depth())
                .height(info.height())
                .ratioWidth(info.ratio().w())
                .ratioDepth(info.ratio().d())
                .ratioHeight(info.ratio().h())
                .quantity(1)
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
