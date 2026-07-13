package kr.co.isajjim.domains.credit.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CreditBalanceResponse(
        @Schema(description = "현재 크레딧 잔액", example = "50000")
        Long balance
) {
}
