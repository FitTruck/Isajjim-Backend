package kr.co.isajjim.domains.credit.presentation.controller;

import kr.co.isajjim.domains.credit.application.response.CreditBalanceResponse;
import kr.co.isajjim.domains.credit.application.response.CreditTransactionResponse;
import kr.co.isajjim.domains.credit.application.usecase.AdminCreditUseCase;
import kr.co.isajjim.domains.credit.presentation.api.AdminCreditApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/partners/{userId}/credits")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCreditController implements AdminCreditApi {

    private final AdminCreditUseCase adminCreditUseCase;

    @Override
    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<CreditBalanceResponse>> getBalance(
            @PathVariable Long userId
    ) {
        CreditBalanceResponse response = adminCreditUseCase.getBalance(userId);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<Page<CreditTransactionResponse>>> getHistory(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        Page<CreditTransactionResponse> response = adminCreditUseCase.getHistory(userId, pageable);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }
}
