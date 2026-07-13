package kr.co.isajjim.domains.credit.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreditChargeReadyRequest(
        @Schema(description = "충전 요청 금액 (원)", example = "50000")
        @NotNull
        @Positive
        Long amount
) {
}
