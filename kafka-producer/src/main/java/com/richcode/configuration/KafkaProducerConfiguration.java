package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
class KafkaProducerConfiguration {

    private static final StringSerializer STRING_SERIALIZER = new StringSerializer();
    private static final JsonSerializer<PurchaseEvent> PURCHASE_EVENT_JSON_SERIALIZER = new JsonSerializer<>();

    private final KafkaProperties kafkaProperties;

    @Bean
    @ConditionalOnProperty(name = "kafka.producer.strategy", havingValue = "at-most-once", matchIfMissing = true)
    public ProducerFactory<String, PurchaseEvent> atMostOnceProducerFactory() {
        return new DefaultKafkaProducerFactory<>(atMostOnceProducerStrategy(), STRING_SERIALIZER, PURCHASE_EVENT_JSON_SERIALIZER);
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.producer.strategy", havingValue = "at-least-once")
    public ProducerFactory<String, PurchaseEvent> atLeastOnceProducerFactory() {
        return new DefaultKafkaProducerFactory<>(atLeastOnceProducerStrategy(), STRING_SERIALIZER, PURCHASE_EVENT_JSON_SERIALIZER);
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.producer.strategy", havingValue = "exactly-once")
    public ProducerFactory<String, PurchaseEvent> exactlyOnceProducerFactory() {
        return new DefaultKafkaProducerFactory<>(exactlyOnceProducerStrategy(), STRING_SERIALIZER, PURCHASE_EVENT_JSON_SERIALIZER);
    }

    @Bean
    public KafkaTemplate<String, PurchaseEvent> kafkaTemplate(final ProducerFactory<String, PurchaseEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    public Map<String, Object> atMostOnceProducerStrategy() {
        return Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress(),
            ProducerConfig.ACKS_CONFIG, "0",
            ProducerConfig.LINGER_MS_CONFIG, 2,
            ProducerConfig.BATCH_SIZE_CONFIG, 32_768,
            ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4",
            ProducerConfig.BUFFER_MEMORY_CONFIG, 33_554_432
        );
    }

    public Map<String, Object> atLeastOnceProducerStrategy() {
        return Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress(),
            ProducerConfig.ACKS_CONFIG, "1",
            ProducerConfig.LINGER_MS_CONFIG, 2,
            ProducerConfig.BATCH_SIZE_CONFIG, 32_768
        );
    }

    public Map<String, Object> exactlyOnceProducerStrategy() {
        return Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress(),
            ProducerConfig.ACKS_CONFIG, "all",
            ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1,
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true",
            ProducerConfig.RETRIES_CONFIG, 64
        );
    }

}
