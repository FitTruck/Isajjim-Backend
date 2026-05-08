package kr.co.isajjim.domains.chat.domain.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import kr.co.isajjim.domains.chat.application.dto.response.ChatMessageResponse;
import kr.co.isajjim.domains.chat.persistence.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmNotificationService {

    private final DeviceTokenRepository deviceTokenRepository;

    public void sendMessageNotification(Long recipientId, ChatMessageResponse message) {
        deviceTokenRepository.findByUserId(recipientId).ifPresent(deviceToken -> {
            try {
                Message fcmMessage = Message.builder()
                        .setToken(deviceToken.getToken())
                        .setNotification(Notification.builder()
                                .setTitle("새 메시지")
                                .setBody(message.content())
                                .build())
                        .putData("roomId", String.valueOf(message.roomId()))
                        .build();
                FirebaseMessaging.getInstance().send(fcmMessage);
            } catch (FirebaseMessagingException e) {
                log.error("FCM 전송 실패 - recipientId: {}", recipientId, e);
            }
        });
    }
}
