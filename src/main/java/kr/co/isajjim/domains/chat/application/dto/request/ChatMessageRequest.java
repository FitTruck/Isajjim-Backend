package kr.co.isajjim.domains.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.isajjim.domains.chat.domain.constant.MessageType;

public record ChatMessageRequest(
        @NotBlank @Size(max = 2000) String content,
        @NotNull MessageType type
) {
}
