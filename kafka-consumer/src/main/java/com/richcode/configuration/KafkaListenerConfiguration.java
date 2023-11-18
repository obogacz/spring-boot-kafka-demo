package com.richcode.configuration;

import com.richcode.consumer.EventConsumer;
import com.richcode.listener.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.richcode.StrategyConfiguration.*;

@Configuration
class KafkaListenerConfiguration {

    @Bean
    @ConditionalOnBean({ AtMostOnceConsumerStrategy.class, SingleEventListenerStrategy.class })
    public SingleEventListener atMostOnceSingleEventListener(final EventConsumer eventConsumer) {
        return new AtMostOnceSingleEventListener(eventConsumer);
    }

    @Bean
    @ConditionalOnBean({ AtMostOnceConsumerStrategy.class, BatchEventListenerStrategy.class })
    public BatchEventListener atMostOnceBatchEventListener(final EventConsumer eventConsumer) {
        return new AtMostOnceBatchEventListener(eventConsumer);
    }

    @Bean
    @ConditionalOnBean({ AtLeastOnceConsumerStrategy.class, SingleEventListenerStrategy.class })
    public SingleEventListener atLeastOnceSingleEventListener(final EventConsumer eventConsumer) {
        return new AtLeastOnceSingleEventListener(eventConsumer);
    }

    @Bean
    @ConditionalOnBean({ AtLeastOnceConsumerStrategy.class, BatchEventListenerStrategy.class })
    public BatchEventListener atLeastOnceBatchEventListener(final EventConsumer eventConsumer) {
        return new AtLeastOnceBatchEventListener(eventConsumer);
    }

    @Bean
    @ConditionalOnBean({ ExactlyOnceConsumerStrategy.class, SingleEventListenerStrategy.class })
    public SingleEventListener exactlyOnceSingleEventListener(final EventConsumer eventConsumer) {
        return new ExactlyOnceSingleEventListener(eventConsumer);
    }

    @Bean
    @ConditionalOnBean({ ExactlyOnceConsumerStrategy.class, BatchEventListenerStrategy.class })
    public BatchEventListener exactlyOnceBatchEventListener(final EventConsumer eventConsumer) {
        return new ExactlyOnceBatchEventListener(eventConsumer);
    }

}
