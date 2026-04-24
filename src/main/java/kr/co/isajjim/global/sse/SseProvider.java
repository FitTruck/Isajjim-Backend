package kr.co.isajjim.global.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseProvider {

    private static final Long DEFAULT_TIMEOUT = 10 * 60 * 1000L;
    private static final String DEFAULT_EVENT_NAME = "sse";
    private static final String DEFAULT_EVENT_CREATED = "EventStream Created.";

    private final EmitterRepository emitterRepository;

    public SseEmitter createEmitter(String key) {
        String emitterId = makeTimeIncludeId(key);
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        // 연결이 이뤄진 후 하나의 데이터도 전송되지 않는다면, 유효 시간이 끝나면 503이 응답되는 문제가 있음.
        sendToClient(emitter, emitterId, DEFAULT_EVENT_CREATED);

        return emitter;
    }

    public void sendEvent(String key, Object data) {
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByEstimateId(key);
        emitters.forEach((id, emitter) -> sendToClient(emitter, id, data));
    }

    //===== HELPER METHOD =====
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name(SseProvider.DEFAULT_EVENT_NAME)
                    .data(data)
            );
        } catch (IOException e) {
            emitterRepository.deleteById(id);
            log.warn("SSE Connection Disconnected. id: {}", id);
        }
    }

    private String makeTimeIncludeId(String key) {
        return key + ":" + System.currentTimeMillis();
    }
}
