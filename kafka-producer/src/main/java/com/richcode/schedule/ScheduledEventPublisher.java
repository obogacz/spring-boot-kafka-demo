package com.richcode.schedule;

import com.richcode.domain.PurchaseEvent;
import com.richcode.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty("scheduled.send-event-fix-delay")
@RequiredArgsConstructor
class ScheduledEventPublisher {

    private final EventPublisher eventPublisher;

    @Scheduled(fixedDelayString = "${scheduled.send-event-fix-delay}")
    public void sendEvent() {
        eventPublisher.send(PurchaseEvent.builder()
            .uuid(UUID.randomUUID())
            .userId("scheduler")
            .productId(String.valueOf((int) (Math.random() * 1000)))
            .comment("Generated from scheduled event publisher")
            .build());
    }

}
