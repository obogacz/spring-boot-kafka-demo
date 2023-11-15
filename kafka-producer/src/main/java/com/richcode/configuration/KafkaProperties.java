package com.richcode.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KafkaProperties {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.producer.strategy:at-most-once}")
    private String producerDeliveryStrategy;

    @Value(value = "${kafka.producer.publishing:async}")
    private String producerPublishingMode;

    @Value(value = "${kafka.topics.purchases.name}")
    private String topicPurchasesName;

    @Value(value = "${kafka.topics.purchases.partitions:1}")
    private int topicPurchasesPartitions;

    @Value(value = "${kafka.topics.purchases.replicas:1}")
    private short topicPurchasesReplicas;

}
