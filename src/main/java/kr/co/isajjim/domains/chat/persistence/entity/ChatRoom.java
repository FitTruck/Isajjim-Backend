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
        uniqueConstraints = @UniqueConstraint(name = "uk_chat_room", columnNames = {"user_id", "vendor_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(length = 2000)
    private String lastMessageContent;

    private LocalDateTime lastMessageAt;

    @Column(nullable = false)
    private int userUnreadCount;

    @Column(nullable = false)
    private int vendorUnreadCount;

    public static ChatRoom create(Long userId, Long vendorId) {
        ChatRoom room = new ChatRoom();
        room.userId = userId;
        room.vendorId = vendorId;
        return room;
    }

    public void updateLastMessage(String content) {
        this.lastMessageContent = content;
        this.lastMessageAt = LocalDateTime.now();
    }

    public void incrementUnreadCount(Long senderId) {
        if (senderId.equals(userId)) {
            this.vendorUnreadCount++;
        } else {
            this.userUnreadCount++;
        }
    }

    public void resetUnreadCount(Long readerId) {
        if (readerId.equals(userId)) {
            this.userUnreadCount = 0;
        } else {
            this.vendorUnreadCount = 0;
        }
    }

    public Long getRecipientId(Long senderId) {
        return senderId.equals(userId) ? vendorId : userId;
    }
}
