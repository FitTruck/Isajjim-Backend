package kr.co.isajjim.domains.chat.application.dto.response;

import kr.co.isajjim.domains.chat.persistence.entity.ChatMessage;
import org.springframework.data.domain.Slice;

import java.util.List;

public record ChatMessagePageResponse(
        List<ChatMessageResponse> messages,
        boolean hasNext,
        int page,
        int size
) {
    public static ChatMessagePageResponse from(Slice<ChatMessage> slice) {
        return new ChatMessagePageResponse(
                slice.getContent().stream().map(ChatMessageResponse::from).toList(),
                slice.hasNext(),
                slice.getNumber(),
                slice.getSize()
        );
    }
}
