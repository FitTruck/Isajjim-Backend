package kr.co.isajjim.domains.user.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.isajjim.domains.user.domain.constant.Role;
import kr.co.isajjim.domains.user.domain.constant.UserStatus;
import kr.co.isajjim.global.security.constant.SocialProvider;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        @Schema(description = "유저 ID", example = "1")
        Long id,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "역할", example = "USER")
        Role role,

        @Schema(description = "소셜 로그인 제공자", example = "KAKAO")
        SocialProvider socialProvider,

        @Schema(description = "계정 상태", example = "ACTIVE")
        UserStatus status,

        @Schema(description = "프로필 이미지 URL")
        String profileImageUrl,

        @Schema(description = "가입일시")
        LocalDateTime createdDate
) {
}
