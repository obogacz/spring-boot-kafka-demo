package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import com.richcode.publisher.AsyncEventPublisher;
import com.richcode.publisher.EventPublisher;
import com.richcode.publisher.SyncEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
class KafkaProducerConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, PurchaseEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.getSafeSettings());
    }

    @Bean
    public KafkaTemplate<String, PurchaseEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.producer", havingValue = "async")
    public EventPublisher asyncEventPublisher() {
        return new AsyncEventPublisher(kafkaProperties, kafkaTemplate());
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.producer", havingValue = "sync")
    public EventPublisher syncEventPublisher() {
        return new SyncEventPublisher(kafkaProperties, kafkaTemplate());
    }

}
