package kr.co.isajjim.domains.estimate.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import kr.co.isajjim.domains.image.application.response.ImageResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EstimateDetailResponse(
        @Schema(description = "생성일", example = "2026-01-01")
        LocalDate createdDate,

        @Schema(description = "견적서 ID", example = "1")
        Long estimateId,

        @Schema(description = "출발지")
        LocationDetailResponse startLocation,

        @Schema(description = "도착지")
        LocationDetailResponse endLocation,

        @Schema(description = "이사 희망 날짜")
        LocalDate preferredMovingDate,

        @Schema(description = "AI 처리 상태", example = "PENDING")
        AIStatus aiStatus,

        @Schema(description = "이미지 목록")
        List<ImageResponse> images,

        @Schema(description = "견적 목록")
        List<EstimateItemResponse> items
) {
}
