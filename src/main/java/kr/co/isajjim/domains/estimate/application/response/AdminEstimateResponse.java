package kr.co.isajjim.domains.estimate.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.estimate.domain.constant.AIStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record AdminEstimateResponse(
        @Schema(description = "견적서 ID", example = "1")
        Long estimateId,

        @Schema(description = "신청자 유저 ID", example = "1")
        Long userId,

        @Schema(description = "신청자 이름", example = "홍길동")
        String userName,

        @Schema(description = "신청자 이메일", example = "user@example.com")
        String userEmail,

        @Schema(description = "AI 처리 상태", example = "PENDING")
        AIStatus aiStatus,

        @Schema(description = "이사 희망 날짜")
        LocalDate preferredMovingDate,

        @Schema(description = "신청 일시")
        LocalDateTime createdDate,

        @Schema(description = "최종 수정 일시")
        LocalDateTime updatedAt
) {
}
