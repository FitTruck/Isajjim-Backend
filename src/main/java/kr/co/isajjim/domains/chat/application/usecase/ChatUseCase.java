package kr.co.isajjim.domains.chat.application.usecase;

import kr.co.isajjim.domains.chat.application.dto.request.ChatRoomCreateRequest;
import kr.co.isajjim.domains.chat.application.dto.response.ChatMessagePageResponse;
import kr.co.isajjim.domains.chat.application.dto.response.ChatRoomResponse;
import kr.co.isajjim.domains.chat.domain.service.ChatService;
import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;
import kr.co.isajjim.domains.chat.persistence.entity.DeviceToken;
import kr.co.isajjim.domains.chat.persistence.repository.DeviceTokenRepository;
import kr.co.isajjim.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class ChatUseCase {

    private final ChatService chatService;
    private final DeviceTokenRepository deviceTokenRepository;

    public ChatRoomResponse getOrCreateRoom(Long userId, Long vendorId) {
        ChatRoom room = chatService.getOrCreateRoom(userId, vendorId);
        return ChatRoomResponse.of(room, userId);
    }

    public List<ChatRoomResponse> getChatRooms(Long userId) {
        return chatService.getChatRooms(userId).stream()
                .map(room -> ChatRoomResponse.of(room, userId))
                .toList();
    }

    public ChatMessagePageResponse getMessages(Long roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ChatMessagePageResponse.from(chatService.getMessages(roomId, pageable));
    }

    public void markAsRead(Long roomId, Long userId) {
        chatService.markAsRead(roomId, userId);
    }

    public void saveDeviceToken(Long userId, String fcmToken) {
        DeviceToken deviceToken = deviceTokenRepository.findByUserId(userId)
                .map(dt -> { dt.update(fcmToken); return dt; })
                .orElseGet(() -> DeviceToken.create(userId, fcmToken));
        deviceTokenRepository.save(deviceToken);
    }
}
