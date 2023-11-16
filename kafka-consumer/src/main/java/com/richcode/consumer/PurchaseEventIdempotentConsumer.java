package com.richcode.consumer;

import com.richcode.cache.PurchaseEventCacheRepository;
import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class PurchaseEventIdempotentConsumer implements EventConsumer {

    private final PurchaseEventCacheRepository cacheRepository;

    @Override
    @Transactional
    public void consume(final PurchaseEvent event) {
        if (isAlreadyProcessed(event)) {
            log.info("Event has been already processed: {}", event);
            return;
        }

        // processing logic

        cacheRepository.save(event);
    }

    private boolean isAlreadyProcessed(final PurchaseEvent event) {
        return cacheRepository.exists(event.uuid());
    }

}
