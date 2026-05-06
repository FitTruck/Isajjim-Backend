package kr.co.isajjim.domains.user.presentation.controller;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.user.presentation.api.UserApi;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.dto.ReissueRequest;
import kr.co.isajjim.global.security.token.JwtProvider;
import kr.co.isajjim.global.security.token.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final JwtProvider jwtProvider;

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> tokenReissue(@RequestBody @Valid ReissueRequest request) {
        TokenResponse tokenResponse = jwtProvider.reissueTokens(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, tokenResponse));
    }

    @Deprecated
    @GetMapping("/auth/success")
    public ResponseEntity<ApiResponse<TokenResponse>> loginSuccess(TokenResponse tokenResponse) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, tokenResponse));
    }
}
