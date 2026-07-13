package kr.co.isajjim.domains.credit.application.usecase;

import kr.co.isajjim.domains.credit.application.mapper.CreditMapper;
import kr.co.isajjim.domains.credit.application.request.CreditChargeConfirmRequest;
import kr.co.isajjim.domains.credit.application.request.CreditChargeReadyRequest;
import kr.co.isajjim.domains.credit.application.response.CreditBalanceResponse;
import kr.co.isajjim.domains.credit.application.response.CreditChargeConfirmResponse;
import kr.co.isajjim.domains.credit.application.response.CreditChargeReadyResponse;
import kr.co.isajjim.domains.credit.application.response.CreditTransactionResponse;
import kr.co.isajjim.domains.credit.domain.service.CreditChargeOrderService;
import kr.co.isajjim.domains.credit.domain.service.CreditLedgerService;
import kr.co.isajjim.domains.credit.persistence.entity.CreditChargeOrder;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.infra.toss.application.dto.TossConfirmResponse;
import kr.co.isajjim.infra.toss.domain.service.TossPaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreditUseCase {

    @Value("${infra.toss.client-key}")
    private String clientKey;

    @Value("${infra.toss.success-url}")
    private String successUrl;

    @Value("${infra.toss.fail-url}")
    private String failUrl;

    @Value("${infra.toss.krw-per-credit}")
    private Long krwPerCredit;

    private final CreditChargeOrderService creditChargeOrderService;
    private final CreditLedgerService creditLedgerService;
    private final TossPaymentClient tossPaymentClient;
    private final UserService userService;

    @Transactional
    public CreditChargeReadyResponse readyCharge(Long userId, CreditChargeReadyRequest request) {
        validateAmount(request.amount());

        UserEntity user = userService.getUserById(userId);
        String orderId = generateOrderId();
        Long creditAmount = request.amount() / krwPerCredit;
        String orderName = String.format("크레딧 충전 %,d원", request.amount());

        CreditChargeOrder order = creditChargeOrderService.createReady(user, orderId, orderName, request.amount(), creditAmount);
        return CreditMapper.toChargeReadyResponse(order, clientKey, successUrl, failUrl);
    }

    // Toss 결제 승인 API 호출(최대 rest-client.read-timeout 소요)을 트랜잭션 밖에서 수행하기 위해
    // 클래스 레벨의 readOnly 트랜잭션을 이 메서드에서만 명시적으로 걷어낸다.
    // 실제 DB 변경(주문 완료 처리, 잔액 반영)은 각각 별도의 짧은 트랜잭션을 가진 서비스 메서드에서 이뤄진다.
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public CreditChargeConfirmResponse confirmCharge(Long userId, CreditChargeConfirmRequest request) {
        CreditChargeOrder readyOrder = creditChargeOrderService.getReadyOrThrow(request.orderId(), userId);
        if (!readyOrder.getAmount().equals(request.amount())) {
            throw new BaseException(ResponseCode.CREDIT_CHARGE_AMOUNT_MISMATCH);
        }

        TossConfirmResponse tossResponse;
        try {
            tossResponse = tossPaymentClient.confirm(request.paymentKey(), request.orderId(), request.amount());
        } catch (RuntimeException e) {
            creditChargeOrderService.fail(request.orderId(), userId, e.getMessage());
            throw (e instanceof BaseException) ? e : new BaseException(ResponseCode.TOSS_PAYMENT_CONFIRM_FAILED);
        }

        LocalDateTime approvedAt = parseApprovedAt(tossResponse.approvedAt());
        CreditChargeOrder completedOrder = creditChargeOrderService.completeAndCredit(
                request.orderId(), userId, tossResponse.paymentKey(), tossResponse.status(), approvedAt
        );
        Long balance = creditLedgerService.getBalance(userId);

        return CreditMapper.toChargeConfirmResponse(completedOrder, balance);
    }

    public CreditBalanceResponse getBalance(Long userId) {
        return CreditMapper.toBalanceResponse(creditLedgerService.getBalance(userId));
    }

    public Page<CreditTransactionResponse> getHistory(Long userId, Pageable pageable) {
        return creditLedgerService.getHistory(userId, pageable)
                .map(CreditMapper::fromCreditTransaction);
    }

    private void validateAmount(Long amount) {
        if (amount == null || amount <= 0 || amount % krwPerCredit != 0) {
            throw new BaseException(ResponseCode.INVALID_CREDIT_CHARGE_AMOUNT);
        }
    }

    private String generateOrderId() {
        return "credit_" + UUID.randomUUID().toString().replace("-", "");
    }

    private LocalDateTime parseApprovedAt(String approvedAt) {
        if (approvedAt == null) {
            return null;
        }
        return OffsetDateTime.parse(approvedAt).toLocalDateTime();
    }
}
