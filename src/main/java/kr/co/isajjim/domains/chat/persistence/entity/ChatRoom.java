package kr.co.isajjim.domains.chat.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "chat_room",
        uniqueConstraints = @UniqueConstraint(name = "uk_chat_room", columnNames = {"creator_id", "target_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(length = 2000)
    private String lastMessageContent;

    private LocalDateTime lastMessageAt;

    @Column(nullable = false)
    private int creatorUnreadCount;

    @Column(nullable = false)
    private int targetUnreadCount;

    public static ChatRoom create(Long creatorId, Long targetId) {
        ChatRoom room = new ChatRoom();
        room.creatorId = creatorId;
        room.targetId = targetId;
        return room;
    }

    public void updateLastMessage(String content) {
        this.lastMessageContent = content;
        this.lastMessageAt = LocalDateTime.now();
    }

    public void incrementUnreadCount(Long senderId) {
        if (senderId.equals(creatorId)) {
            this.targetUnreadCount++;
        } else {
            this.creatorUnreadCount++;
        }
    }

    public void resetUnreadCount(Long readerId) {
        if (readerId.equals(creatorId)) {
            this.creatorUnreadCount = 0;
        } else {
            this.targetUnreadCount = 0;
        }
    }

    public Long getRecipientId(Long senderId) {
        return senderId.equals(creatorId) ? targetId : creatorId;
    }

    public boolean isParticipant(Long userId) {
        return this.creatorId.equals(userId) || this.targetId.equals(userId);
    }

    public int getUnreadCountFor(Long userId) {
        return userId.equals(creatorId) ? creatorUnreadCount : targetUnreadCount;
    }
}
