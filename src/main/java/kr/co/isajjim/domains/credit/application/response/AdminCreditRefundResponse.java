package kr.co.isajjim.domains.credit.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminCreditRefundResponse(
        @Schema(description = "크레딧 충전 주문 ID", example = "1")
        Long chargeOrderId,

        @Schema(description = "유저 ID", example = "1")
        Long userId,

        @Schema(description = "환불 처리된 크레딧 수량", example = "50000")
        Long refundedCredit,

        @Schema(description = "환불 후 잔액", example = "0")
        Long balance,

        @Schema(description = "Toss 결제취소 처리 일시")
        LocalDateTime canceledAt
) {
}
