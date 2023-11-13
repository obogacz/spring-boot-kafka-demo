package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import com.richcode.publisher.AsyncEventPublisher;
import com.richcode.publisher.EventPublisher;
import com.richcode.publisher.SyncEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
class KafkaPublisherConfiguration {

    @Bean
    @ConditionalOnProperty(name = "kafka.producer.publishing", havingValue = "async", matchIfMissing = true)
    public EventPublisher asyncEventPublisher(final KafkaProperties kafkaProperties,
                                              final KafkaTemplate<String, PurchaseEvent> kafkaTemplate) {
        return new AsyncEventPublisher(kafkaProperties, kafkaTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.producer.publishing", havingValue = "sync")
    public EventPublisher syncEventPublisher(final KafkaProperties kafkaProperties,
                                             final KafkaTemplate<String, PurchaseEvent> kafkaTemplate) {
        return new SyncEventPublisher(kafkaProperties, kafkaTemplate);
    }

}
