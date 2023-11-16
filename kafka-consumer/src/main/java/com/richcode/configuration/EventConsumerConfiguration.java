package com.richcode.configuration;

import com.richcode.cache.OffsetCacheRepository;
import com.richcode.cache.PurchaseEventCacheRepository;
import com.richcode.consumer.EventConsumer;
import com.richcode.consumer.PurchaseEventConsumer;
import com.richcode.consumer.PurchaseEventIdempotentConsumer;
import com.richcode.domain.PurchaseEvent;
import org.infinispan.Cache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static com.richcode.cache.OffsetCacheRepository.*;

@Configuration
class EventConsumerConfiguration {

    @Bean
    @ConditionalOnProperty(name = "kafka.consumer.idempotent", havingValue = "false", matchIfMissing = true)
    public EventConsumer eventConsumer() {
        return new PurchaseEventConsumer();
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.consumer.idempotent", havingValue = "true")
    public EventConsumer eventIdempotentConsumer(final OffsetCacheRepository offsetCacheRepository,
                                                 final PurchaseEventCacheRepository purchaseEventCacheRepository) {
        return new PurchaseEventIdempotentConsumer(offsetCacheRepository, purchaseEventCacheRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.consumer.idempotent", havingValue = "true")
    public OffsetCacheRepository offsetCacheRepository(final Cache<String, Offset> cache) {
        return new OffsetCacheRepository(cache);
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.consumer.idempotent", havingValue = "true")
    public PurchaseEventCacheRepository purchaseEventCacheRepository(final Cache<UUID, PurchaseEvent> cache) {
        return new PurchaseEventCacheRepository(cache);
    }

}
