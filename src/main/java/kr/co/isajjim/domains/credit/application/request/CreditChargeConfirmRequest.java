package kr.co.isajjim.domains.credit.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreditChargeConfirmRequest(
        @Schema(description = "Toss 결제 키", example = "5EnNZRJGvaBX7zk2yd8ydw2G")
        @NotBlank
        String paymentKey,

        @Schema(description = "충전 주문 ID (ready 응답에서 발급받은 값)", example = "credit_a1b2c3d4")
        @NotBlank
        String orderId,

        @Schema(description = "결제 금액 (원, ready 요청 시 금액과 일치해야 함)", example = "50000")
        @NotNull
        @Positive
        Long amount
) {
}
