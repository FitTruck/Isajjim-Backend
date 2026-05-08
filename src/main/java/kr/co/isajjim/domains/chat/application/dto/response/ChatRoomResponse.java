package kr.co.isajjim.domains.chat.application.dto.response;

import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomResponse(
        Long roomId,
        Long userId,
        Long vendorId,
        String lastMessageContent,
        LocalDateTime lastMessageAt,
        int unreadCount
) {
    public static ChatRoomResponse of(ChatRoom room, Long myId) {
        int unreadCount = myId.equals(room.getUserId())
                ? room.getUserUnreadCount()
                : room.getVendorUnreadCount();
        return new ChatRoomResponse(
                room.getId(),
                room.getUserId(),
                room.getVendorId(),
                room.getLastMessageContent(),
                room.getLastMessageAt(),
                unreadCount
        );
    }
}
