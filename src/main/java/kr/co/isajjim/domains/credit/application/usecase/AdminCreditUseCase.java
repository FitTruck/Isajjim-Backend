package kr.co.isajjim.domains.credit.application.usecase;

import kr.co.isajjim.domains.credit.application.mapper.CreditMapper;
import kr.co.isajjim.domains.credit.application.response.CreditBalanceResponse;
import kr.co.isajjim.domains.credit.application.response.CreditTransactionResponse;
import kr.co.isajjim.domains.credit.domain.service.CreditLedgerService;
import kr.co.isajjim.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCreditUseCase {

    private final CreditLedgerService creditLedgerService;

    public CreditBalanceResponse getBalance(Long userId) {
        return CreditMapper.toBalanceResponse(creditLedgerService.getBalance(userId));
    }

    public Page<CreditTransactionResponse> getHistory(Long userId, Pageable pageable) {
        return creditLedgerService.getHistory(userId, pageable)
                .map(CreditMapper::fromCreditTransaction);
    }
}
