package kr.co.isajjim.global.security.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.api.AuthApi;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import kr.co.isajjim.global.security.constant.SocialProvider;
import kr.co.isajjim.global.security.dto.SignUpRequest;
import kr.co.isajjim.global.security.token.TokenResponse;
import kr.co.isajjim.global.security.usecase.OAuth2UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController implements AuthApi {

    private final OAuth2UseCase oAuth2UseCase;

    @Override
    @PostMapping("/oauth/register")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<ApiResponse<TokenResponse>> oauthRegister(
            @RequestParam SocialProvider provider,
            @RequestBody @Valid SignUpRequest.Oidc request) {
        TokenResponse response = oAuth2UseCase.signUp(provider, request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, response));
    }

    @Override
    @GetMapping("/agree-terms")
    public ResponseEntity<ApiResponse<Boolean>> getTermsAgreed(
            @AuthenticationPrincipal CustomUserDetails user) {
        boolean agreed = oAuth2UseCase.getTermsAgreed(user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK, agreed));
    }

    @Override
    @PostMapping("/agree-terms")
    public ResponseEntity<ApiResponse<Void>> agreeTerms(
            @AuthenticationPrincipal CustomUserDetails user) {
        oAuth2UseCase.agreeTerms(user.getUserId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logOut(
            @RequestParam(required = false) String refreshToken,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        oAuth2UseCase.logOut(user.getUserId(), refreshToken);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ResponseCode.OK));
    }
}
