package kr.co.isajjim.domains.credit.domain.service;

import kr.co.isajjim.domains.credit.domain.constant.CreditChargeStatus;
import kr.co.isajjim.domains.credit.persistence.entity.CreditChargeOrder;
import kr.co.isajjim.domains.credit.persistence.repository.CreditChargeOrderRepository;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreditChargeOrderService {

    private final CreditChargeOrderRepository creditChargeOrderRepository;
    private final CreditLedgerService creditLedgerService;

    @Transactional
    public CreditChargeOrder createReady(UserEntity user, String orderId, String orderName, Long amount, Long creditAmount) {
        CreditChargeOrder order = CreditChargeOrder.create(user, orderId, orderName, amount, creditAmount);
        return creditChargeOrderRepository.save(order);
    }

    // 잠금 없이 조회 + 상태 사전 검증. Toss 승인 API를 호출하기 전에 걸러내어 불필요한 외부 호출과 변조된 금액 확인을 막는다.
    public CreditChargeOrder getReadyOrThrow(String orderId, Long userId) {
        CreditChargeOrder order = getOrThrow(orderId, userId);
        if (order.getStatus() != CreditChargeStatus.READY) {
            throw new BaseException(ResponseCode.ALREADY_PROCESSED_CREDIT_CHARGE_ORDER);
        }
        return order;
    }

    // Toss 승인 성공 이후 행을 다시 잠금 조회해 최종 상태를 재확인한다. 진짜 동시 이중 confirm은 여기서 걸러진다.
    // 주문 완료 처리와 잔액 반영을 하나의 트랜잭션으로 묶어, 둘 중 하나만 반영되는 상황을 방지한다.
    @Transactional
    public CreditChargeOrder completeAndCredit(String orderId, Long userId, String paymentKey, String tossStatus, LocalDateTime approvedAt) {
        CreditChargeOrder order = getOrThrowForUpdate(orderId, userId);
        if (order.getStatus() != CreditChargeStatus.READY) {
            throw new BaseException(ResponseCode.ALREADY_PROCESSED_CREDIT_CHARGE_ORDER);
        }
        order.complete(paymentKey, tossStatus, approvedAt);
        creditLedgerService.chargeBalance(userId, order.getCreditAmount(), "TOSS_CHARGE", order.getId());
        return order;
    }

    @Transactional
    public void fail(String orderId, Long userId, String failReason) {
        creditChargeOrderRepository.findByOrderIdAndUser_IdForUpdate(orderId, userId)
                .filter(order -> order.getStatus() == CreditChargeStatus.READY)
                .ifPresent(order -> order.fail(failReason));
    }

    private CreditChargeOrder getOrThrow(String orderId, Long userId) {
        return creditChargeOrderRepository.findByOrderIdAndUser_Id(orderId, userId)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_CREDIT_CHARGE_ORDER));
    }

    private CreditChargeOrder getOrThrowForUpdate(String orderId, Long userId) {
        return creditChargeOrderRepository.findByOrderIdAndUser_IdForUpdate(orderId, userId)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_CREDIT_CHARGE_ORDER));
    }
}
