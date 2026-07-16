package kr.co.isajjim.infra.toss.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Toss 결제 승인 응답은 이 외에도 훨씬 많은 필드를 담고 있으나, 이 도메인에서 실제로 사용하는 값만 매핑한다.
// approvedAt은 오프셋 포함 ISO-8601 문자열(예: "2024-01-01T00:00:00+09:00")로 내려오므로 그대로 문자열로 받아 서비스 계층에서 파싱한다.
@JsonIgnoreProperties(ignoreUnknown = true)
public record TossConfirmResponse(
        String paymentKey,
        String orderId,
        Long totalAmount,
        String status,
        String approvedAt
) {
}
