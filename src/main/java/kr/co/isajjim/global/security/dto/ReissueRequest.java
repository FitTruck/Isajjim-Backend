package kr.co.isajjim.global.security.dto;

import jakarta.validation.constraints.NotBlank;

public record ReissueRequest(
    @NotBlank
    String refreshToken
) {
}