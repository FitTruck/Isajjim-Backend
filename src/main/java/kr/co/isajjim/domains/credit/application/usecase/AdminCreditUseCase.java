package kr.co.isajjim.domains.credit.application.usecase;

import kr.co.isajjim.domains.credit.application.mapper.CreditMapper;
import kr.co.isajjim.domains.credit.application.request.AdminCreditRefundRequest;
import kr.co.isajjim.domains.credit.application.response.AdminCreditBalanceOverviewResponse;
import kr.co.isajjim.domains.credit.application.response.AdminCreditRefundResponse;
import kr.co.isajjim.domains.credit.application.response.AdminCreditTransactionResponse;
import kr.co.isajjim.domains.credit.application.response.CreditBalanceResponse;
import kr.co.isajjim.domains.credit.application.response.CreditTransactionResponse;
import kr.co.isajjim.domains.credit.domain.service.CreditChargeOrderService;
import kr.co.isajjim.domains.credit.domain.service.CreditLedgerService;
import kr.co.isajjim.domains.credit.persistence.entity.CreditChargeOrder;
import kr.co.isajjim.domains.credit.persistence.entity.CreditTransaction;
import kr.co.isajjim.domains.user.domain.constant.ApprovalStatus;
import kr.co.isajjim.domains.user.domain.service.PartnerProfileService;
import kr.co.isajjim.global.annotation.UseCase;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import kr.co.isajjim.infra.toss.domain.service.TossPaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCreditUseCase {

    private final CreditLedgerService creditLedgerService;
    private final CreditChargeOrderService creditChargeOrderService;
    private final TossPaymentClient tossPaymentClient;
    private final PartnerProfileService partnerProfileService;

    public CreditBalanceResponse getBalance(Long userId) {
        return CreditMapper.toBalanceResponse(creditLedgerService.getBalance(userId));
    }

    public Page<CreditTransactionResponse> getHistory(Long userId, Pageable pageable) {
        return creditLedgerService.getHistory(userId, pageable)
                .map(CreditMapper::fromCreditTransaction);
    }

    // 어드민 - 승인된 파트너 전체의 크레딧 잔액 현황 (업체명/대표자명 검색 지원)
    public Page<AdminCreditBalanceOverviewResponse> getBalanceOverview(String keyword, Pageable pageable) {
        return partnerProfileService.getList(ApprovalStatus.APPROVED, keyword, pageable)
                .map(profile -> CreditMapper.toBalanceOverviewResponse(
                        profile.getUser().getId(),
                        profile.getCompanyName(),
                        profile.getRepresentativeName(),
                        creditLedgerService.getBalance(profile.getUser().getId())
                ));
    }

    // 어드민 - 전체 파트너의 충전/소모/환불 거래내역 (업체명/대표자명 검색 지원)
    public Page<AdminCreditTransactionResponse> getAllTransactions(String keyword, Pageable pageable) {
        Page<CreditTransaction> page;
        if (keyword == null || keyword.isBlank()) {
            page = creditLedgerService.getAllHistory(pageable);
        } else {
            List<Long> userIds = partnerProfileService.findApprovedUserIdsByKeyword(keyword.trim());
            page = userIds.isEmpty() ? Page.empty(pageable) : creditLedgerService.getHistoryByUserIds(userIds, pageable);
        }
        return page.map(CreditMapper::toAdminTransactionResponse);
    }

    // Toss 결제취소 API 호출(외부 HTTP) 동안 잔액/주문 행 락을 들고 있지 않도록,
    // confirmCharge와 동일하게 이 메서드만 트랜잭션 밖에서 실행한다.
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AdminCreditRefundResponse refundCharge(Long chargeOrderId, AdminCreditRefundRequest request) {
        CreditChargeOrder order = creditChargeOrderService.getDoneOrThrow(chargeOrderId);

        try {
            tossPaymentClient.cancel(order.getPaymentKey(), request.reason());
        } catch (RuntimeException e) {
            throw (e instanceof BaseException) ? e : new BaseException(ResponseCode.TOSS_PAYMENT_CANCEL_FAILED);
        }

        LocalDateTime canceledAt = LocalDateTime.now();
        CreditChargeOrder refundedOrder = creditChargeOrderService.refundAndDeduct(chargeOrderId, request.reason(), canceledAt);
        Long balance = creditLedgerService.getBalance(refundedOrder.getUser().getId());

        return CreditMapper.toAdminRefundResponse(refundedOrder, balance);
    }
}
