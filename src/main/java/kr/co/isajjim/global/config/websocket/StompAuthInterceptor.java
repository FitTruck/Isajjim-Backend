package kr.co.isajjim.global.config.websocket;

import kr.co.isajjim.global.common.Constants;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.security.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompAuthInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final Map<String, Authentication> sessionAuthMap = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token == null || !token.startsWith(Constants.BEARER)) {
                throw new MessageDeliveryException(message, ResponseCode.UNAUTHORIZED.name());
            }
            Authentication auth;
            try {
                auth = jwtProvider.getAuthentication(token.substring(Constants.BEARER.length()));
            } catch (Exception e) {
                throw new MessageDeliveryException(message, ResponseCode.UNAUTHORIZED.name());
            }
            sessionAuthMap.put(accessor.getSessionId(), auth);
            accessor.setUser(auth);
            log.info("[WS] CONNECT sessionId={} user={}", accessor.getSessionId(), auth.getName());
            return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
        }

        if (StompCommand.DISCONNECT.equals(command)) {
            log.info("[WS] DISCONNECT user={}", getUsername(accessor));
            sessionAuthMap.remove(accessor.getSessionId());
            return message;
        }

        // SUBSCRIBE, SEND 등 이후 명령에 simpUser를 주입해 SecurityContextHolder 연동
        Authentication auth = sessionAuthMap.get(accessor.getSessionId());
        if (auth != null) {
            accessor.setUser(auth);
            if (StompCommand.SUBSCRIBE.equals(command)) {
                log.info("[WS] SUBSCRIBE destination={} user={}", accessor.getDestination(), auth.getName());
            }
            if (StompCommand.SEND.equals(command)) {
                log.info("[WS] SEND destination={} user={}", accessor.getDestination(), auth.getName());
            }
            return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
        }

        return message;
    }

    private String getUsername(StompHeaderAccessor accessor) {
        Authentication auth = sessionAuthMap.get(accessor.getSessionId());
        return auth != null ? auth.getName() : "unknown";
    }
}
