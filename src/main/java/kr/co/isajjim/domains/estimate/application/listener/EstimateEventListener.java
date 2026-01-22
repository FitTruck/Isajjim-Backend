package kr.co.isajjim.domains.estimate.application.listener;

import kr.co.isajjim.domains.estimate.domain.event.EstimateCreatedEvent;
import kr.co.isajjim.infra.ai.domain.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EstimateEventListener {

    private final AIService aiService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEstimateCreated(EstimateCreatedEvent event) {
        aiService.analyzeFurniture(event.estimateId(), event.imageDtos());
    }
}