package kr.co.isajjim.domains.credit.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AdminCreditRefundRequest(
        @Schema(description = "환불(결제취소) 사유", example = "고객 요청에 의한 충전 취소")
        @NotBlank
        String reason
) {
}
