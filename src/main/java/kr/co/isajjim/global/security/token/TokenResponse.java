package kr.co.isajjim.global.security.token;

import lombok.Builder;

@Builder
public record TokenResponse(
        Long userId,
        String accessToken,
        String refreshToken
) {
}