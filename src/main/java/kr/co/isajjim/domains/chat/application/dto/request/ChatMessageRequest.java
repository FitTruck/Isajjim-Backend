package kr.co.isajjim.domains.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.isajjim.domains.chat.domain.constant.MessageType;

public record ChatMessageRequest(
        @NotBlank String content,
        @NotNull MessageType type
) {
}
