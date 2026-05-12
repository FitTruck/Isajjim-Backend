package kr.co.isajjim.domains.chat.domain.service;

import com.google.firebase.messaging.*;
import kr.co.isajjim.domains.chat.application.dto.response.ChatMessageResponse;
import kr.co.isajjim.domains.chat.domain.constant.MessageType;
import kr.co.isajjim.domains.chat.persistence.entity.DeviceToken;
import kr.co.isajjim.domains.chat.persistence.repository.DeviceTokenRepository;
import kr.co.isajjim.domains.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmNotificationService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final DeviceTokenService deviceTokenService;
    private final UserService userService;

    public void sendMessageNotification(Long recipientId, ChatMessageResponse message) {
        List<DeviceToken> deviceTokens = deviceTokenRepository.findAllByUserId(recipientId);
        if (deviceTokens.isEmpty()) return;

        List<String> tokens = deviceTokens.stream().map(DeviceToken::getToken).toList();
        String senderName = userService.getUserById(message.senderId()).getName();
        String body = message.type() == MessageType.IMAGE ? MessageType.IMAGE.label : message.content();

        MulticastMessage fcmMessage = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle(senderName)
                        .setBody(body)
                        .build())
                .putData("roomId", String.valueOf(message.roomId()))
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(fcmMessage);
            if (response.getFailureCount() > 0) {
                List<String> failedTokens = new ArrayList<>();
                List<SendResponse> responses = response.getResponses();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        log.warn("FCM 전송 실패 - token: {}", tokens.get(i));
                        failedTokens.add(tokens.get(i));
                    }
                }
                deviceTokenService.deleteInvalidTokens(failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("FCM 전송 실패 - recipientId: {}", recipientId, e);
        }
    }
}
