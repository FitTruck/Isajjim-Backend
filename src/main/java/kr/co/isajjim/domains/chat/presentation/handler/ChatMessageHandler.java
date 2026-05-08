package kr.co.isajjim.domains.chat.presentation.handler;

import jakarta.validation.Valid;
import kr.co.isajjim.domains.chat.application.dto.request.ChatMessageRequest;
import kr.co.isajjim.domains.chat.domain.service.ChatService;
import kr.co.isajjim.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import kr.co.isajjim.global.common.ResponseCode;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatMessageHandler {

    private final ChatService chatService;

    @MessageMapping("/chat/rooms/{roomId}/messages")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Payload @Valid ChatMessageRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Map<String, Object> attrs = headerAccessor.getSessionAttributes();
        Authentication auth = (attrs != null) ? (Authentication) attrs.get("authentication") : null;
        if (auth == null) throw new MessageDeliveryException(null, ResponseCode.UNAUTHORIZED.name());
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        chatService.sendMessage(roomId, user.getUserId(), request);
    }
}
