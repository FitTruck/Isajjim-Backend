package kr.co.isajjim.infra.toss.application.dto;

public record TossConfirmRequest(
        String paymentKey,
        String orderId,
        Long amount
) {
}
