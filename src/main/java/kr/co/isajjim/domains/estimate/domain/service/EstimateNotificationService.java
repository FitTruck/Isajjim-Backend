package kr.co.isajjim.domains.estimate.domain.service;

import kr.co.isajjim.global.sse.SseProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class EstimateNotificationService {

    private static final String EVENT_COMPLETED = "COMPLETED";

    private final SseProvider sseProvider;

    public SseEmitter createConnection(Long estimateId) {
        return sseProvider.createEmitter(String.valueOf(estimateId));
    }

    public void sendNotify(Long estimateId) {
        sseProvider.sendEvent(String.valueOf(estimateId), EVENT_COMPLETED);
    }
}
