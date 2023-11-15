package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
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

    private static final ErrorHandlingDeserializer<PurchaseEvent> PURCHASE_EVENT_ERROR_HANDLING_DESERIALIZER =
        new ErrorHandlingDeserializer<>(new JsonDeserializer<>(PurchaseEvent.class));

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, PurchaseEvent> consumerFactory() {
        final Map<String, Object> config = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress(),
            ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumerGroupId()
        );
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), PURCHASE_EVENT_ERROR_HANDLING_DESERIALIZER);
    }

    @Bean(KafkaProperties.KAFKA_PURCHASE_LISTENER_CONTAINER_FACTORY_BEAN)
    public ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        containerFactory.getContainerProperties().setSyncCommits(true);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return containerFactory;
    }

}
