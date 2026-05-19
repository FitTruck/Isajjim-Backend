package kr.co.isajjim.domains.chat.domain.service;

import kr.co.isajjim.domains.chat.application.dto.request.ChatMessageRequest;
import kr.co.isajjim.domains.chat.application.dto.response.ChatMessageResponse;
import kr.co.isajjim.domains.chat.application.dto.response.ChatRoomUpdateEvent;
import kr.co.isajjim.domains.chat.domain.constant.MessageType;
import kr.co.isajjim.domains.chat.persistence.entity.ChatMessage;
import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;
import kr.co.isajjim.domains.chat.persistence.repository.ChatMessageRepository;
import kr.co.isajjim.domains.chat.persistence.repository.ChatRoomRepository;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final FcmNotificationService fcmNotificationService;

    @Transactional
    public void sendMessage(Long roomId, Long senderId, ChatMessageRequest request) {
        ChatRoom room = getOrThrow(roomId);
        if (!room.isParticipant(senderId)) {
            throw new BaseException(ResponseCode.CHAT_ROOM_ACCESS_DENIED);
        }

        ChatMessage message = ChatMessage.create(room, senderId, request.content(), request.type());
        chatMessageRepository.save(message);

        String lastMessagePreview = request.type() == MessageType.IMAGE ? MessageType.IMAGE.label : request.content();
        room.updateLastMessage(lastMessagePreview);
        room.incrementUnreadCount(senderId);

        ChatMessageResponse response = ChatMessageResponse.from(message);
        messagingTemplate.convertAndSend("/sub/chat/rooms/" + roomId, response);

        Long recipientId = room.getRecipientId(senderId);
        messagingTemplate.convertAndSend("/sub/user/" + senderId,   ChatRoomUpdateEvent.of(room, senderId));
        messagingTemplate.convertAndSend("/sub/user/" + recipientId, ChatRoomUpdateEvent.of(room, recipientId));

        fcmNotificationService.sendMessageNotification(recipientId, response);
    }

    @Transactional
    public ChatRoom getOrCreateRoom(Long myId, Long targetId) {
        return chatRoomRepository.findByParticipants(myId, targetId)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.create(myId, targetId)));
    }

    public List<ChatRoom> getChatRooms(Long userId) {
        return chatRoomRepository.findByCreatorIdOrTargetIdOrderByLastMessageAtDesc(userId, userId);
    }

    public Slice<ChatMessage> getMessages(Long roomId, Long userId, Pageable pageable) {
        ChatRoom room = getOrThrow(roomId);
        if (!room.isParticipant(userId)) {
            throw new BaseException(ResponseCode.CHAT_ROOM_ACCESS_DENIED);
        }
        return chatMessageRepository.findByChatRoomIdOrderByCreatedDateDesc(roomId, pageable);
    }

    @Transactional
    public void markAsRead(Long roomId, Long userId) {
        ChatRoom room = getOrThrow(roomId);
        if (!room.isParticipant(userId)) {
            throw new BaseException(ResponseCode.CHAT_ROOM_ACCESS_DENIED);
        }
        room.resetUnreadCount(userId);
    }

    /* HELPER METHOD */
    private ChatRoom getOrThrow(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_CHAT_ROOM));
    }
}
