package kr.co.isajjim.domains.user.presentation.controller;

import kr.co.isajjim.domains.user.application.usecase.UserWithdrawalUseCase;
import kr.co.isajjim.domains.user.presentation.api.UserWithdrawalApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserWithdrawalController implements UserWithdrawalApi {

    private final UserWithdrawalUseCase userWithdrawalUseCase;

    @Override
    @DeleteMapping("/withdrawal")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        userWithdrawalUseCase.withdraw(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }
}