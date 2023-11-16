package com.richcode.configuration;

import com.richcode.cache.OffsetCacheRepository;
import com.richcode.cache.PurchaseEventCacheRepository;
import com.richcode.consumer.EventConsumer;
import com.richcode.consumer.BasicEventConsumer;
import com.richcode.consumer.IdempotentEventConsumer;
import com.richcode.domain.PurchaseEvent;
import org.infinispan.Cache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static com.richcode.cache.OffsetCacheRepository.*;

@Configuration
class EventConsumerConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "kafka.consumer.idempotent", havingValue = "false", matchIfMissing = true)
    static class BasicConsumerConfiguration {

        @Bean
        public EventConsumer eventConsumer() {
            return new BasicEventConsumer();
        }

    }

    @Configuration
    @ConditionalOnProperty(name = "kafka.consumer.idempotent", havingValue = "true")
    static class IdempotentConsumerConfiguration {

        @Bean
        public EventConsumer eventIdempotentConsumer(final OffsetCacheRepository offsetCacheRepository,
                                                     final PurchaseEventCacheRepository purchaseEventCacheRepository) {
            return new IdempotentEventConsumer(offsetCacheRepository, purchaseEventCacheRepository);
        }

        @Bean
        public OffsetCacheRepository offsetCacheRepository(final Cache<String, Offset> cache) {
            return new OffsetCacheRepository(cache);
        }

        @Bean
        public PurchaseEventCacheRepository purchaseEventCacheRepository(final Cache<UUID, PurchaseEvent> cache) {
            return new PurchaseEventCacheRepository(cache);
        }

    }

}
