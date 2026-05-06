package kr.co.isajjim.global.security.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kr.co.isajjim.global.annotation.swagger.ApiErrorResponseExplanation;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import kr.co.isajjim.global.security.constant.SocialProvider;
import kr.co.isajjim.global.security.dto.SignUpRequest;
import kr.co.isajjim.global.security.token.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface AuthApi {

    @Operation(
            summary = "소셜로그인 회원가입(로그인)",
            description = "OAuth2 OIDC 회원가입(로그인) 요청"
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = TokenResponse.class,
                    description = "회원가입(로그인) 성공"
            ),
            errors = {
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.NEED_REGISTER),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.EXPIRED_JWT_TOKEN),
                    @ApiErrorResponseExplanation(exceptionCode = ResponseCode.INVALID_JWT_TOKEN)
            }
    )
    ResponseEntity<ApiResponse<TokenResponse>> oauthRegister(
            @RequestParam SocialProvider provider,
            @RequestBody @Valid SignUpRequest.Oidc request);

    @Operation(
            summary = "로그아웃",
            description = "(Nullable) refreshToken 전송 시 서버에서 폐기합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "로그아웃 성공"
            )
    )
    ResponseEntity<ApiResponse<Void>> logOut(
            @RequestParam(required = false) String refreshToken,
            @AuthenticationPrincipal CustomUserDetails user
    );
}
