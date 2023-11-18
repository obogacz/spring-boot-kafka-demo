package com.richcode.consumer;

import com.richcode.cache.ProcessedOffsetCacheRepository;
import com.richcode.cache.PurchaseEventCacheRepository;
import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class IdempotentEventConsumer implements EventConsumer {

    private final PurchaseEventCacheRepository purchaseEventCacheRepository;
    private final ProcessedOffsetCacheRepository processedOffsetCacheRepository;

    @Override
    @Transactional
    public void consume(final ConsumerRecord<String, PurchaseEvent> event) {
        if (isAlreadyProcessed(event)) {
            log.info("Event has been already processed: {}", event);
            return;
        }

        process(event.value());

        purchaseEventCacheRepository.save(event);
        processedOffsetCacheRepository.save(event);

        log.info("[CONSUMED EVENT] topic: {}, partition: {}, offset: {}, event: {}",
            event.topic(), event.partition(), event.offset(), event.value());
    }

    private boolean isAlreadyProcessed(final ConsumerRecord<String, PurchaseEvent> event) {
        return purchaseEventCacheRepository.exists(event);
    }

    private void process(final PurchaseEvent event) {

        // processing logic

    }

}
