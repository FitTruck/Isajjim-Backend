package kr.co.isajjim.domains.user.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.isajjim.domains.user.application.dto.request.ProfileImageRequest;
import kr.co.isajjim.global.annotation.swagger.ApiResponseExplanations;
import kr.co.isajjim.global.annotation.swagger.ApiSuccessResponseExplanation;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import kr.co.isajjim.global.security.dto.ReissueRequest;
import kr.co.isajjim.global.security.token.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "유저 API")
public interface UserApi {

    @Operation(summary = "토큰 재발급", description = "refreshToken을 이용해 accessToken을 재발급합니다.<br>" +
            "refreshToken의 만료 기간이 일정 기준 이하이면 refreshToken이 함께 재발급됩니다. (프론트에서 교체 요망, 기준 미충족하는 경우 accessToken만 반환됨)"
    )
    @ApiResponseExplanations(success = @ApiSuccessResponseExplanation(responseClass = TokenResponse.class, description = "재발급 성공"))
    ResponseEntity<ApiResponse<TokenResponse>> tokenReissue(
            @RequestBody @Valid ReissueRequest request);

    @Operation(summary = "프로필 이미지 저장", description = "POST /api/v1/presigned-url로 발급받은 fileUrl을 전달해 프로필 이미지를 저장합니다.")
    ResponseEntity<ApiResponse<Void>> updateProfileImage(
            @RequestBody @Valid ProfileImageRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user);

    @Operation(summary = "프로필 이미지 삭제", description = "프로필 이미지를 S3에서 삭제하고 초기화합니다.")
    ResponseEntity<ApiResponse<Void>> deleteProfileImage(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails user);
}
