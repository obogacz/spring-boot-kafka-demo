package com.richcode.schedule;

import com.richcode.domain.PurchaseEvent;
import com.richcode.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
class ScheduledEventPublisher {

    private final EventPublisher asyncEventPublisher;

    @Scheduled(fixedDelay = 2000)
    public void sendEvent() {
        asyncEventPublisher.send(PurchaseEvent.builder()
            .uuid(UUID.randomUUID())
            .userId("scheduler")
            .productId(String.valueOf((int) (Math.random() * 1000)))
            .build());
    }

}
