package com.richcode.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KafkaProperties {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.producer.strategy}")
    private String producerDeliveryStrategy;

    @Value(value = "${kafka.producer.publishing}")
    private String producerPublishingMode;

    @Value(value = "${kafka.topics.purchases.name}")
    private String topicPurchasesName;

    @Value(value = "${kafka.topics.purchases.partitions}")
    private int topicPurchasesPartitions;

    @Value(value = "${kafka.topics.purchases.replicas}")
    private short topicPurchasesReplicas;

}
