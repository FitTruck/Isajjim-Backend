package kr.co.isajjim.domains.estimate.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.image.application.response.ImageResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record EstimateDetailResponse(
        @Schema(description = "견적서 ID", example = "1")
        Long estimateId,

        @Schema(description = "AI 처리 상태", example = "PENDING")
        AIStatus aiStatus,

        @Schema(description = "이미지 목록")
        List<ImageResponse> images,

        @Schema(description = "견적 목록")
        List<EstimateItemResponse> items
) {
}
