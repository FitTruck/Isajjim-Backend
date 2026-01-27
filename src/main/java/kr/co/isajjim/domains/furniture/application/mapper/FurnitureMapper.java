package kr.co.isajjim.domains.furniture.application.mapper;

import kr.co.isajjim.domains.furniture.FurnitureResponse;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureType;
import kr.co.isajjim.domains.furniture.persistence.entity.Furniture;
import kr.co.isajjim.infra.ai.application.dto.AIAnalysisResponse;

public class FurnitureMapper {
    public static Furniture toFurniture(
            int quantity,
            AIAnalysisResponse.FurnitureInfo info,
            FurnitureType bestType,
            double volume
    ) {
        return Furniture.builder()
                .label(info.label())
                .type(bestType)
                .width(bestType.getWidth())
                .depth(bestType.getDepth())
                .volume(volume)
//                .ratioWidth(info.ratio().w())
//                .ratioDepth(info.ratio().d())
//                .ratioHeight(info.ratio().h())
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