package kr.co.isajjim.domains.credit.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.credit.domain.constant.CreditTransactionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreditTransactionResponse(
        @Schema(description = "거래 ID", example = "1")
        Long transactionId,

        @Schema(description = "거래 유형", example = "CHARGE")
        CreditTransactionType type,

        @Schema(description = "거래 크레딧 수량 (항상 양수)", example = "50000")
        Long creditAmount,

        @Schema(description = "거래 처리 직후 잔액", example = "50000")
        Long balanceAfter,

        @Schema(description = "거래 출처 태그", example = "TOSS_CHARGE")
        String referenceType,

        @Schema(description = "거래 출처 참조 ID", example = "1")
        Long referenceId,

        @Schema(description = "거래 일시")
        LocalDateTime createdDate
) {
}
