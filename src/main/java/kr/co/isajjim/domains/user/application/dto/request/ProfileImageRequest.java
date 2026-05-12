package kr.co.isajjim.domains.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProfileImageRequest(
        @NotBlank
        String fileUrl
) {
}
