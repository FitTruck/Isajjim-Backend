package kr.co.isajjim.domains.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeviceTokenRequest(
        @NotBlank String fcmToken
) {
}
