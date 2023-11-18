package com.richcode.configuration;

import com.richcode.cache.ProcessedOffsetCacheRepository;
import com.richcode.domain.PurchaseEvent;
import com.richcode.listener.RebalanceListener;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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

import static com.richcode.StrategyConfiguration.*;
import static org.springframework.kafka.listener.ContainerProperties.*;

@EnableKafka
@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(ExactlyOnceConsumerStrategy.class)
public class KafkaExactlyOnceConsumerConfiguration {

    public static final String SINGLE_EVENT_LISTENER_CONTAINER_FACTORY_BEAN_NAME = "kafkaPurchaseExactlyOnceSingleEventListenerContainerFactory";
    public static final String BATCH_EVENT_LISTENER_CONTAINER_FACTORY_BEAN_NAME = "kafkaPurchaseExactlyOnceBatchEventListenerContainerFactory";

    private final KafkaProperties kafkaProperties;

    @Bean
    ConsumerFactory<String, PurchaseEvent> consumerFactory() {
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

    @Bean(SINGLE_EVENT_LISTENER_CONTAINER_FACTORY_BEAN_NAME)
    @ConditionalOnBean(SingleEventListenerStrategy.class)
    ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> singleEventListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        containerFactory.getContainerProperties().setSyncCommits(true);
        containerFactory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
        return containerFactory;
    }

    @Bean(BATCH_EVENT_LISTENER_CONTAINER_FACTORY_BEAN_NAME)
    @ConditionalOnBean(BatchEventListenerStrategy.class)
    ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> batchEventListenerContainerFactory(final ProcessedOffsetCacheRepository processedOffsetCacheRepository) {
        ConcurrentKafkaListenerContainerFactory<String, PurchaseEvent> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        containerFactory.setBatchListener(true);
        containerFactory.getContainerProperties().setSyncCommits(true);
        containerFactory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
        containerFactory.getContainerProperties().setConsumerRebalanceListener(new RebalanceListener(processedOffsetCacheRepository));
        return containerFactory;
    }

}
