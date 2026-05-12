package kr.co.isajjim.domains.chat.application.dto.response;

import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomResponse(
        Long roomId,
        Long creatorId,
        Long targetId,
        String lastMessageContent,
        LocalDateTime lastMessageAt,
        int unreadCount
) {
    public static ChatRoomResponse of(ChatRoom room, Long myId) {
        return new ChatRoomResponse(
                room.getId(),
                room.getCreatorId(),
                room.getTargetId(),
                room.getLastMessageContent(),
                room.getLastMessageAt(),
                room.getUnreadCountFor(myId)
        );
    }
}
