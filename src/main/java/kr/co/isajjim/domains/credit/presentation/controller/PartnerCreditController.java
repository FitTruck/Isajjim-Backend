package kr.co.isajjim.domains.credit.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.credit.application.request.CreditChargeConfirmRequest;
import kr.co.isajjim.domains.credit.application.request.CreditChargeReadyRequest;
import kr.co.isajjim.domains.credit.application.response.CreditBalanceResponse;
import kr.co.isajjim.domains.credit.application.response.CreditChargeConfirmResponse;
import kr.co.isajjim.domains.credit.application.response.CreditChargeReadyResponse;
import kr.co.isajjim.domains.credit.application.response.CreditTransactionResponse;
import kr.co.isajjim.domains.credit.application.usecase.CreditUseCase;
import kr.co.isajjim.domains.credit.presentation.api.PartnerCreditApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/partner-credits")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PARTNER')")
public class PartnerCreditController implements PartnerCreditApi {

    private final CreditUseCase creditUseCase;

    @Override
    @PostMapping("/charges/ready")
    public ResponseEntity<ApiResponse<CreditChargeReadyResponse>> readyCharge(
            @RequestBody @Valid CreditChargeReadyRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        CreditChargeReadyResponse response = creditUseCase.readyCharge(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @PostMapping("/charges/confirm")
    public ResponseEntity<ApiResponse<CreditChargeConfirmResponse>> confirmCharge(
            @RequestBody @Valid CreditChargeConfirmRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        CreditChargeConfirmResponse response = creditUseCase.confirmCharge(user.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<CreditBalanceResponse>> getBalance(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        CreditBalanceResponse response = creditUseCase.getBalance(user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<Page<CreditTransactionResponse>>> getHistory(
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Page<CreditTransactionResponse> response = creditUseCase.getHistory(user.getUserId(), pageable);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }
}
