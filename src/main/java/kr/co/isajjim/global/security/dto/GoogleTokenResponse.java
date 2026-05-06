package kr.co.isajjim.global.security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleTokenResponse(
                String accessToken,
                Long expiresIn,
                String refreshToken,
                String scope,
                String tokenType,
                String idToken) {
}
