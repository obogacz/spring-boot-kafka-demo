package com.richcode.consumer;

import com.richcode.cache.OffsetCacheRepository;
import com.richcode.cache.PurchaseEventCacheRepository;
import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class IdempotentEventConsumer implements EventConsumer {

    private final OffsetCacheRepository offsetCacheRepository;
    private final PurchaseEventCacheRepository eventCacheRepository;

    @Override
    @Transactional
    public void consume(final ConsumerRecord<String, PurchaseEvent> event) {
        if (isAlreadyProcessed(event.value())) {
            log.info("Event has been already processed: {}", event);
            return;
        }

        // processing logic

        eventCacheRepository.save(event.value());
        offsetCacheRepository.save(event.topic(), event.partition(), event.offset());

        log.info("[CONSUMED EVENT] topic: {}, partition: {}, offset: {}, event: {}",
            event.topic(), event.partition(), event.offset(), event.value());
    }

    private boolean isAlreadyProcessed(final PurchaseEvent event) {
        return eventCacheRepository.exists(event.uuid());
    }

}
