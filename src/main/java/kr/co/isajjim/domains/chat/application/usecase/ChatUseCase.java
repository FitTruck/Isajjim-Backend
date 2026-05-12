package kr.co.isajjim.domains.chat.application.usecase;

import kr.co.isajjim.domains.chat.application.dto.response.ChatMessagePageResponse;
import kr.co.isajjim.domains.chat.application.dto.response.ChatRoomResponse;
import kr.co.isajjim.domains.chat.domain.service.ChatService;
import kr.co.isajjim.domains.chat.persistence.entity.ChatRoom;
import kr.co.isajjim.domains.user.domain.service.UserService;
import kr.co.isajjim.domains.user.persistence.entity.UserEntity;

import java.util.List;
import java.util.Map;
import kr.co.isajjim.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@UseCase
@RequiredArgsConstructor
public class ChatUseCase {

    private final ChatService chatService;
    private final UserService userService;

    public ChatRoomResponse getOrCreateRoom(Long myId, Long targetId) {
        ChatRoom room = chatService.getOrCreateRoom(myId, targetId);
        UserEntity target = userService.getUserById(targetId(room, myId));
        return ChatRoomResponse.of(room, myId, target);
    }

    public List<ChatRoomResponse> getChatRooms(Long myId) {
        List<ChatRoom> rooms = chatService.getChatRooms(myId);
        List<Long> targetIds = rooms.stream().map(room -> targetId(room, myId)).toList();
        Map<Long, UserEntity> targetMap = userService.getUserMapByIds(targetIds);
        return rooms.stream()
                .map(room -> ChatRoomResponse.of(room, myId, targetMap.get(targetId(room, myId))))
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

    private Long targetId(ChatRoom room, Long myId) {
        return myId.equals(room.getCreatorId()) ? room.getTargetId() : room.getCreatorId();
    }
}
