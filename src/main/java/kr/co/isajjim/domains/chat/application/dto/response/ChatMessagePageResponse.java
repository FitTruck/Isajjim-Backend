package kr.co.isajjim.domains.chat.application.dto.response;

import kr.co.isajjim.domains.chat.persistence.entity.ChatMessage;
import org.springframework.data.domain.Page;

import java.util.List;

public record ChatMessagePageResponse(
        List<ChatMessageResponse> messages,
        boolean hasNext,
        int page,
        int size
) {
    public static ChatMessagePageResponse from(Page<ChatMessage> page) {
        return new ChatMessagePageResponse(
                page.getContent().stream().map(ChatMessageResponse::from).toList(),
                page.hasNext(),
                page.getNumber(),
                page.getSize()
        );
    }
}
