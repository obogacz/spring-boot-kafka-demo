package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
class KafkaConsumerConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, PurchaseEvent> consumerFactory() {
        final Deserializer<String> keyDeserializer = new StringDeserializer();
        final Deserializer<PurchaseEvent> valueDeserializer = new ErrorHandlingDeserializer<>(new JsonDeserializer<>(PurchaseEvent.class));
        final Map<String, Object> config = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress(),
            ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumerGroupId(),
            ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "5000",
            ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, "5000"
        );
        return new DefaultKafkaConsumerFactory<>(config, keyDeserializer, valueDeserializer);
    }

    @Bean(KafkaProperties.KAFKA_PURCHASE_SINGLE_EVENT_LISTENER_CONTAINER_FACTORY_BEAN)
    @ConditionalOnProperty(name = "kafka.consumer.processing-type", havingValue = "single", matchIfMissing = true)
    public ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> kafkaSingleEventListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        containerFactory.getContainerProperties().setSyncCommits(true);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return containerFactory;
    }

    @Bean(KafkaProperties.KAFKA_PURCHASE_BATCH_EVENT_LISTENER_CONTAINER_FACTORY_BEAN)
    @ConditionalOnProperty(name = "kafka.consumer.processing-type", havingValue = "batch")
    public ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> kafkaBatchEventListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        containerFactory.setBatchListener(true);
        containerFactory.getContainerProperties().setSyncCommits(true);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        // todo
//        containerFactory.getContainerProperties().setConsumerRebalanceListener();

        return containerFactory;
    }

}
