package kr.co.isajjim.infra.ai.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.isajjim.domains.furniture.domain.constant.FurnitureLabel;

import java.util.List;

public record AIAnalysisResponse(
        List<ImageResult> results
) {
    public record ImageResult(
            @JsonProperty("image_id")
            Long imageId,
            List<FurnitureInfo> objects
    ) {
    }

    public record FurnitureInfo(
            FurnitureLabel label,
            Double width,
            Double depth,
            Double height
//            FurnitureRatio ratio
    ) {
    }
//
//    public record FurnitureRatio(
//            Double w,
//            Double d,
//            Double h
//    ) {
//    }
}