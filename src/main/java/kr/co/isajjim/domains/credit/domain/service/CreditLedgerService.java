package kr.co.isajjim.domains.credit.domain.service;

import kr.co.isajjim.domains.credit.domain.constant.CreditTransactionType;
import kr.co.isajjim.domains.credit.persistence.entity.CreditBalance;
import kr.co.isajjim.domains.credit.persistence.entity.CreditTransaction;
import kr.co.isajjim.domains.credit.persistence.repository.CreditBalanceRepository;
import kr.co.isajjim.domains.credit.persistence.repository.CreditTransactionRepository;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditLedgerService {

    private final CreditBalanceRepository creditBalanceRepository;
    private final CreditTransactionRepository creditTransactionRepository;
    private final UserService userService;

    // 잔액 조회 전용 (GET 응답). 잔액 행이 아직 없으면 0을 반환하며 행을 생성하지 않는다.
    public Long getBalance(Long userId) {
        return creditBalanceRepository.findByUser_Id(userId)
                .map(CreditBalance::getBalance)
                .orElse(0L);
    }

    public Page<CreditTransaction> getHistory(Long userId, Pageable pageable) {
        return creditTransactionRepository.findByUser_Id(userId, pageable);
    }

    // 어드민 - 전체 파트너 거래내역 (검색어 없음)
    public Page<CreditTransaction> getAllHistory(Pageable pageable) {
        return creditTransactionRepository.findAll(pageable);
    }

    // 어드민 - 특정 유저 ID 목록(업체명 검색 결과)으로 좁힌 거래내역
    public Page<CreditTransaction> getHistoryByUserIds(List<Long> userIds, Pageable pageable) {
        return creditTransactionRepository.findByUser_IdIn(userIds, pageable);
    }

    @Transactional
    public Long chargeBalance(Long userId, Long creditAmount, String referenceType, Long referenceId) {
        CreditBalance balance = getOrCreateForUpdate(userId);
        balance.increase(creditAmount);
        creditTransactionRepository.save(
                CreditTransaction.charge(balance.getUser(), creditAmount, balance.getBalance(), referenceType, referenceId)
        );
        return balance.getBalance();
    }

    // 향후 "견적서 발송" 등에서 이 서비스를 직접 주입받아 호출할 크레딧 소모 메서드.
    @Transactional
    public Long consume(Long userId, Long creditAmount, String referenceType, Long referenceId) {
        return decrease(userId, creditAmount, CreditTransactionType.CONSUME, referenceType, referenceId);
    }

    // 어드민의 Toss 결제취소(환불) 승인 이후, 해당 금액만큼 크레딧 잔액을 차감한다.
    @Transactional
    public Long refund(Long userId, Long creditAmount, String referenceType, Long referenceId) {
        return decrease(userId, creditAmount, CreditTransactionType.REFUND, referenceType, referenceId);
    }

    private Long decrease(Long userId, Long creditAmount, CreditTransactionType type, String referenceType, Long referenceId) {
        if (creditAmount == null || creditAmount <= 0) {
            throw new IllegalArgumentException("차감할 크레딧 수량은 0보다 커야 합니다.");
        }

        CreditBalance balance = getOrCreateForUpdate(userId);
        if (balance.getBalance() < creditAmount) {
            throw new BaseException(ResponseCode.INSUFFICIENT_CREDIT);
        }

        balance.decrease(creditAmount);
        CreditTransaction transaction = type == CreditTransactionType.REFUND
                ? CreditTransaction.refund(balance.getUser(), creditAmount, balance.getBalance(), referenceType, referenceId)
                : CreditTransaction.consume(balance.getUser(), creditAmount, balance.getBalance(), referenceType, referenceId);
        creditTransactionRepository.save(transaction);
        return balance.getBalance();
    }

    private CreditBalance getOrCreateForUpdate(Long userId) {
        return creditBalanceRepository.findByUser_IdForUpdate(userId)
                .orElseGet(() -> {
                    createIfAbsent(userId);
                    return creditBalanceRepository.findByUser_IdForUpdate(userId)
                            .orElseThrow(() -> new BaseException(ResponseCode.INTERNAL_SERVER_ERROR));
                });
    }

    // 잔액 행을 별도의 새 트랜잭션에서 생성한다. 동시 최초-생성 경합으로 unique(user_id) 제약이 걸려도
    // 이 트랜잭션만 롤백되고, 호출부의 원래 트랜잭션(잠금 재조회)은 영향받지 않는다.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createIfAbsent(Long userId) {
        if (creditBalanceRepository.findByUser_Id(userId).isPresent()) {
            return;
        }
        UserEntity user = userService.getUserById(userId);
        try {
            creditBalanceRepository.saveAndFlush(CreditBalance.createEmpty(user));
        } catch (DataIntegrityViolationException e) {
            // 동시에 다른 트랜잭션이 먼저 생성을 완료한 경우. 호출부에서 재조회하므로 무시한다.
        }
    }
}
