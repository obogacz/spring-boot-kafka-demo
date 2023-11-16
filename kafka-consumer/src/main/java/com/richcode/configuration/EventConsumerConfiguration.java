package com.richcode.configuration;

import com.richcode.cache.PurchaseEventCacheRepository;
import com.richcode.consumer.EventConsumer;
import com.richcode.consumer.PurchaseEventConsumer;
import com.richcode.consumer.PurchaseEventIdempotentConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EventConsumerConfiguration {

    @Bean
    @ConditionalOnProperty(name = "kafka.consumer.processing-type", havingValue = "false", matchIfMissing = true)
    public EventConsumer eventConsumer() {
        return new PurchaseEventConsumer();
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.consumer.idempotent", havingValue = "true")
    public EventConsumer eventIdempotentConsumer(final PurchaseEventCacheRepository repository) {
        return new PurchaseEventIdempotentConsumer(repository);
    }

}
