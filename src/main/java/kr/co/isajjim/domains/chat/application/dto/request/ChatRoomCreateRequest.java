package kr.co.isajjim.domains.chat.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChatRoomCreateRequest(
        @NotNull Long targetId
) {
}
