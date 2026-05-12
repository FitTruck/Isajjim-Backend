package kr.co.isajjim.domains.chat.application.dto.response;

import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;

import java.time.LocalDateTime;

public record ChatRoomResponse(
        Long roomId,
        Long creatorId,
        Long targetId,
        ParticipantInfo target,
        String lastMessageContent,
        LocalDateTime lastMessageAt,
        int unreadCount
) {
    public record ParticipantInfo(String name) {
        public static ParticipantInfo from(UserEntity user) {
            return new ParticipantInfo(user.getName());
        }
    }

    public static ChatRoomResponse of(ChatRoom room, Long myId, UserEntity target) {
        return new ChatRoomResponse(
                room.getId(),
                room.getCreatorId(),
                room.getTargetId(),
                ParticipantInfo.from(target),
                room.getLastMessageContent(),
                room.getLastMessageAt(),
                room.getUnreadCountFor(myId)
        );
    }
}
