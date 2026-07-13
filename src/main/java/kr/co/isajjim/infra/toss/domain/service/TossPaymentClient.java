package kr.co.isajjim.infra.toss.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.infra.toss.application.dto.TossConfirmRequest;
import kr.co.isajjim.infra.toss.application.dto.TossConfirmResponse;
import kr.co.isajjim.infra.toss.application.dto.TossErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentClient {

    @Value("${infra.toss.base-url}")
    private String baseUrl;

    @Value("${infra.toss.secret-key}")
    private String secretKey;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public TossConfirmResponse confirm(String paymentKey, String orderId, Long amount) {
        TossConfirmRequest request = new TossConfirmRequest(paymentKey, orderId, amount);

        return restClient.post()
                .uri(baseUrl + "/v1/payments/confirm")
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeader())
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, response) -> {
                    String rawBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    logTossError(response.getStatusCode(), rawBody);
                    throw new BaseException(ResponseCode.TOSS_PAYMENT_CONFIRM_FAILED);
                })
                .body(TossConfirmResponse.class);
    }

    private String basicAuthHeader() {
        String credentials = secretKey + ":";
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    // Toss의 4xx 원문은 클라이언트에 그대로 노출하지 않고 서버 로그에만 남긴다.
    private void logTossError(HttpStatusCode status, String rawBody) {
        try {
            TossErrorResponse error = objectMapper.readValue(rawBody, TossErrorResponse.class);
            log.error("Toss 결제 승인 실패. status={}, code={}, message={}", status, error.code(), error.message());
        } catch (Exception e) {
            log.error("Toss 결제 승인 실패. status={}, body={}", status, rawBody);
        }
    }
}
