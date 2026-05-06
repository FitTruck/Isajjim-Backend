package kr.co.isajjim.domains.user.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "User")
public interface UserWithdrawalApi {

    @Operation(summary = "회원 탈퇴", description = "애플리케이션에서 회원을 탈퇴시키고 소셜 로그인 연결을 해제합니다.")
    ResponseEntity<ApiResponse<Void>> withdraw(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails);
}
