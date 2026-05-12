package kr.co.isajjim.domains.chat.application.usecase;

import kr.co.isajjim.domains.chat.application.dto.response.ChatMessagePageResponse;
import kr.co.isajjim.domains.chat.application.dto.response.ChatRoomResponse;
import kr.co.isajjim.domains.chat.domain.service.ChatService;
import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;
import kr.co.isajjim.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class ChatUseCase {

    private final ChatService chatService;

    public ChatRoomResponse getOrCreateRoom(Long myId, Long targetId) {
        ChatRoom room = chatService.getOrCreateRoom(myId, targetId);
        return ChatRoomResponse.of(room, myId);
    }

    public List<ChatRoomResponse> getChatRooms(Long userId) {
        return chatService.getChatRooms(userId).stream()
                .map(room -> ChatRoomResponse.of(room, userId))
                .toList();
    }

    private static final int MAX_PAGE_SIZE = 100;

    public ChatMessagePageResponse getMessages(Long roomId, int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE));
        return ChatMessagePageResponse.from(chatService.getMessages(roomId, userId, pageable));
    }

    public void markAsRead(Long roomId, Long userId) {
        chatService.markAsRead(roomId, userId);
    }
}
