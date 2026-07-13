package kr.co.isajjim.domains.credit.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CreditChargeReadyResponse(
        @Schema(description = "충전 주문 ID", example = "credit_a1b2c3d4")
        String orderId,

        @Schema(description = "주문명 (Toss 결제창에 표시)", example = "크레딧 충전 50,000원")
        String orderName,

        @Schema(description = "충전 요청 금액 (원)", example = "50000")
        Long amount,

        @Schema(description = "지급될 크레딧 수량", example = "50000")
        Long creditAmount,

        @Schema(description = "Toss 결제위젯 클라이언트 키")
        String clientKey,

        @Schema(description = "결제 성공 시 리다이렉트할 URL")
        String successUrl,

        @Schema(description = "결제 실패 시 리다이렉트할 URL")
        String failUrl
) {
}
