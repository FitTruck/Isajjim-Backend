package kr.co.isajjim.global.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /* 클라이언트가 WebSocket 연결을 맺을 엔드포인트 */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SockJS 프로토콜 지원 추가 (낮은 버전 브라우저 지원)
    }

    /* STOMP 메시지 라우팅 규칙 설정
     * 서버와 클라이언트가 메시지를 주고받을 때 어떤 경로로 처리할지 정의 */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 메시지를 구독(수신)하는 경로
        // ex. /sub/chat/1로 구독하면 1번방 메시지를 받을 수 있음
        registry.enableSimpleBroker("/sub");

        // 클라이언트가 메시지를 발행(송신)하는 경로
        // ex. /pub/chat/1로 보내면 @MessageMapping("/chat/{roomId}") 컨트롤러로 라우팅됨
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
