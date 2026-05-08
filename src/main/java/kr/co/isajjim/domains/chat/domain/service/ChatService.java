package kr.co.isajjim.domains.chat.domain.service;

import kr.co.isajjim.domains.chat.application.dto.request.ChatMessageRequest;
import kr.co.isajjim.domains.chat.application.dto.response.ChatMessageResponse;
import kr.co.isajjim.domains.chat.persistence.entity.ChatMessage;
import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;
import kr.co.isajjim.domains.chat.persistence.repository.ChatMessageRepository;
import kr.co.isajjim.domains.chat.persistence.repository.ChatRoomRepository;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ChatMessageResponse sendMessage(Long roomId, Long senderId, ChatMessageRequest request) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_CHAT_ROOM));

        ChatMessage message = ChatMessage.create(room, senderId, request.content(), request.type());
        chatMessageRepository.save(message);

        room.updateLastMessage(request.content());
        room.incrementUnreadCount(senderId);

        ChatMessageResponse response = ChatMessageResponse.from(message);
        messagingTemplate.convertAndSend("/topic/chat/rooms/" + roomId, response);
        fcmNotificationService.sendMessageNotification(room.getRecipientId(senderId), response);

        return response;
    }

    @Transactional
    public ChatRoom getOrCreateRoom(Long userId, Long vendorId) {
        return chatRoomRepository.findByUserIdAndVendorId(userId, vendorId)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.create(userId, vendorId)));
    }

    public List<ChatRoom> getChatRooms(Long userId) {
        return chatRoomRepository.findByUserIdOrVendorIdOrderByLastMessageAtDesc(userId, userId);
    }

    public Page<ChatMessage> getMessages(Long roomId, Pageable pageable) {
        return chatMessageRepository.findByChatRoomIdOrderByCreatedDateDesc(roomId, pageable);
    }

    @Transactional
    public void markAsRead(Long roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(ResponseCode.NOT_FOUND_CHAT_ROOM));
        room.resetUnreadCount(userId);
    }
}
