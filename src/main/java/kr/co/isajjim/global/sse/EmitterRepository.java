package kr.co.isajjim.global.sse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    public Map<String, SseEmitter> findAllEmitterStartWithByEstimateId(String estimateId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(estimateId + ":"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public void deleteById(String id) {
        emitters.remove(id);
    }
}
