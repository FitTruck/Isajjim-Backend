package kr.co.isajjim.domains.user.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.user.application.dto.request.RoleRequest;
import kr.co.isajjim.domains.user.application.usecase.UserUseCase;
import kr.co.isajjim.domains.user.presentation.api.UserApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import kr.co.isajjim.global.security.dto.ReissueRequest;
import kr.co.isajjim.global.security.token.JwtProvider;
import kr.co.isajjim.global.security.token.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserUseCase userUseCase;
    private final JwtProvider jwtProvider;

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> tokenReissue(@RequestBody @Valid ReissueRequest request) {
        TokenResponse tokenResponse = jwtProvider.reissueTokens(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, tokenResponse));
    }

    @Override
    @PatchMapping("/role")
    public ResponseEntity<ApiResponse<Void>> updateRole(
            @RequestBody @Valid RoleRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        userUseCase.updateRole(user.getUserId(), request.role());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }
}
