package kr.co.isajjim.domains.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNameRequest(
        @NotBlank
        @Size(max = 50, message = "이름은 50자 이하여야 합니다.")
        String name
) {
}
