package kr.co.isajjim.domains.chat.presentation.handler;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.chat.application.dto.request.ChatMessageRequest;
import kr.co.isajjim.domains.chat.domain.service.ChatService;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageHandler {

    private final ChatService chatService;

    @MessageMapping("/chat/rooms/{roomId}/messages")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Payload @Valid ChatMessageRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        chatService.sendMessage(roomId, user.getUserId(), request);
    }
}
