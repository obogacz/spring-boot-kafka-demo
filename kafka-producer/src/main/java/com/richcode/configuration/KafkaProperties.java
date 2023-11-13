package com.richcode.configuration;

import com.richcode.support.PurchaseEventSerializer;
import lombok.Getter;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration
public class KafkaProperties {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.topics.purchases.name}")
    private String topicPurchasesName;

    @Value(value = "${kafka.topics.purchases.partitions}")
    private int topicPurchasesPartitions;

    @Value(value = "${kafka.topics.purchases.replicas}")
    private short topicPurchasesReplicas;

    public Map<String, Object> getFastSettings() {
        final Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ProducerConfig.ACKS_CONFIG, "0");
        config.put(ProducerConfig.LINGER_MS_CONFIG, 2);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 32_768);
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33_554_432);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PurchaseEventSerializer.class);

        return config;
    }

    public Map<String, Object> getSafeSettings() {
        final Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        config.put(ProducerConfig.RETRIES_CONFIG, 64);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PurchaseEventSerializer.class);

        return config;
    }

}
