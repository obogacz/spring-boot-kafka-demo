package com.richcode.configuration;

import com.richcode.consumer.EventConsumer;
import com.richcode.listener.BatchEventListener;
import com.richcode.listener.SingleEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KafkaListenerConfiguration {

    @Bean
    @ConditionalOnProperty(name = "kafka.consumer.processing-type", havingValue = "single", matchIfMissing = true)
    public SingleEventListener singleEventListener(EventConsumer eventConsumer) {
        return new SingleEventListener(eventConsumer);
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.consumer.processing-type", havingValue = "batch")
    public BatchEventListener batchEventListener(EventConsumer eventConsumer) {
        return new BatchEventListener(eventConsumer);
    }

}
