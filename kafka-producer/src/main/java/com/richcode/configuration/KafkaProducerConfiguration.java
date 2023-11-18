package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static com.richcode.StrategyConfiguration.*;

@Configuration
@RequiredArgsConstructor
class KafkaProducerConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    @ConditionalOnBean(AtMostOnceProducerStrategy.class)
    public ProducerFactory<String, PurchaseEvent> atMostOnceProducerFactory() {
        final Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress(),
            ProducerConfig.ACKS_CONFIG, "0",
            ProducerConfig.LINGER_MS_CONFIG, 2,
            ProducerConfig.BATCH_SIZE_CONFIG, 32_768,
            ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4",
            ProducerConfig.BUFFER_MEMORY_CONFIG, 33_554_432
        );
        return new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    @ConditionalOnBean(AtLeastOnceProducerStrategy.class)
    public ProducerFactory<String, PurchaseEvent> atLeastOnceProducerFactory() {
        final Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress(),
            ProducerConfig.ACKS_CONFIG, "1",
            ProducerConfig.LINGER_MS_CONFIG, 2,
            ProducerConfig.BATCH_SIZE_CONFIG, 32_768
        );
        return new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    @ConditionalOnBean(ExactlyOnceProducerStrategy.class)
    public ProducerFactory<String, PurchaseEvent> exactlyOnceProducerFactory() {
        final Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress(),
            ProducerConfig.ACKS_CONFIG, "all",
            ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1,
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true",
            ProducerConfig.RETRIES_CONFIG, 64
        );
        return new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, PurchaseEvent> kafkaTemplate(final ProducerFactory<String, PurchaseEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
