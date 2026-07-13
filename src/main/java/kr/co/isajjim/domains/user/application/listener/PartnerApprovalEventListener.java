package kr.co.isajjim.domains.user.application.listener;

import kr.co.isajjim.domains.chat.domain.service.FcmNotificationService;
import kr.co.isajjim.domains.user.domain.event.PartnerApprovalDecidedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PartnerApprovalEventListener {

    private static final String TITLE = "파트너 신청 결과 안내";
    private static final String APPROVED_BODY = "파트너 신청이 승인되었습니다.";

    private final FcmNotificationService fcmNotificationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePartnerApprovalDecided(PartnerApprovalDecidedEvent event) {
        String body = switch (event.approvalStatus()) {
            case APPROVED -> APPROVED_BODY;
            case REJECTED -> "파트너 신청이 반려되었습니다. 사유: " + event.rejectionReason();
            case PENDING -> throw new IllegalStateException("PENDING 상태는 알림 대상이 아닙니다.");
        };

        fcmNotificationService.sendNotification(
                event.userId(),
                TITLE,
                body,
                Map.of("approvalStatus", event.approvalStatus().name())
        );
    }
}
