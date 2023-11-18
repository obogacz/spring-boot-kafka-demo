package com.richcode.configuration;

import com.richcode.cache.ProcessedOffsetCacheRepository;
import com.richcode.cache.PurchaseEventCacheRepository;
import com.richcode.consumer.BasicEventConsumer;
import com.richcode.consumer.EventConsumer;
import com.richcode.consumer.IdempotentEventConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.richcode.StrategyConfiguration.ExactlyOnceConsumerStrategy;

@Configuration
class EventConsumerConfiguration {


    @Bean
    @ConditionalOnMissingBean(ExactlyOnceConsumerStrategy.class)
    public EventConsumer eventConsumer() {
        return new BasicEventConsumer();
    }

    @Bean
    @ConditionalOnBean(ExactlyOnceConsumerStrategy.class)
    public EventConsumer eventIdempotentConsumer(final ProcessedOffsetCacheRepository processedOffsetCacheRepository,
                                                 final PurchaseEventCacheRepository purchaseEventCacheRepository) {
        return new IdempotentEventConsumer(purchaseEventCacheRepository, processedOffsetCacheRepository);
    }

}
