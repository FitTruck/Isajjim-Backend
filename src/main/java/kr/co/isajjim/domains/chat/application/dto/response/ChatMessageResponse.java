package kr.co.isajjim.domains.chat.application.dto.response;

import kr.co.isajjim.domains.chat.domain.constant.MessageType;
import kr.co.isajjim.domains.chat.persistence.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long messageId,
        Long roomId,
        Long senderId,
        String content,
        MessageType type,
        LocalDateTime createdAt
) {
    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getSenderId(),
                message.getContent(),
                message.getType(),
                message.getCreatedDate()
        );
    }
}
