package kr.co.isajjim.domains.credit.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.credit.application.request.AdminCreditRefundRequest;
import kr.co.isajjim.domains.credit.application.response.AdminCreditBalanceOverviewResponse;
import kr.co.isajjim.domains.credit.application.response.AdminCreditRefundResponse;
import kr.co.isajjim.domains.credit.application.response.AdminCreditTransactionResponse;
import kr.co.isajjim.domains.credit.application.usecase.AdminCreditUseCase;
import kr.co.isajjim.domains.credit.presentation.api.AdminCreditOverviewApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/credits")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCreditOverviewController implements AdminCreditOverviewApi {

    private final AdminCreditUseCase adminCreditUseCase;

    @Override
    @GetMapping("/balances")
    public ResponseEntity<ApiResponse<Page<AdminCreditBalanceOverviewResponse>>> getBalanceOverview(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        Page<AdminCreditBalanceOverviewResponse> response = adminCreditUseCase.getBalanceOverview(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<Page<AdminCreditTransactionResponse>>> getAllTransactions(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        Page<AdminCreditTransactionResponse> response = adminCreditUseCase.getAllTransactions(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @PostMapping("/charges/{chargeOrderId}/refund")
    public ResponseEntity<ApiResponse<AdminCreditRefundResponse>> refundCharge(
            @PathVariable Long chargeOrderId,
            @RequestBody @Valid AdminCreditRefundRequest request
    ) {
        AdminCreditRefundResponse response = adminCreditUseCase.refundCharge(chargeOrderId, request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }
}
