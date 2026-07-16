package kr.co.isajjim.domains.credit.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreditChargeConfirmResponse(
        @Schema(description = "충전 주문 ID", example = "credit_a1b2c3d4")
        String orderId,

        @Schema(description = "이번 충전으로 지급된 크레딧 수량", example = "50000")
        Long chargedCredit,

        @Schema(description = "충전 후 잔액", example = "50000")
        Long balance,

        @Schema(description = "Toss 결제 승인 일시")
        LocalDateTime approvedAt
) {
}
