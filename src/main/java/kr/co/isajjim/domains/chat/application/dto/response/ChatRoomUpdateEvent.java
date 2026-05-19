package kr.co.isajjim.domains.chat.application.dto.response;

import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomUpdateEvent(
        Long roomId,
        String lastMessageContent,
        LocalDateTime lastMessageAt,
        int unreadCount
) {
    public static ChatRoomUpdateEvent of(ChatRoom room, Long userId) {
        return new ChatRoomUpdateEvent(
                room.getId(),
                room.getLastMessageContent(),
                room.getLastMessageAt(),
                room.getUnreadCountFor(userId)
        );
    }
}
