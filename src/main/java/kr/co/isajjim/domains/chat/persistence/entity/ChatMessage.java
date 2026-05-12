package kr.co.isajjim.domains.chat.persistence.entity;

import jakarta.persistence.*;
import kr.co.isajjim.domains.chat.domain.constant.MessageType;
import kr.co.isajjim.global.base.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    public static ChatMessage create(ChatRoom chatRoom, Long senderId, String content, MessageType type) {
        ChatMessage message = new ChatMessage();
        message.chatRoom = chatRoom;
        message.senderId = senderId;
        message.content = content;
        message.type = type;
        return message;
    }
}
