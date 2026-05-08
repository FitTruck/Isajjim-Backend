package kr.co.isajjim.domains.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.isajjim.domains.chat.domain.constant.DeviceType;

public record DeviceTokenRequest(
        @NotBlank String token,
        @NotNull DeviceType deviceType
) {
}
