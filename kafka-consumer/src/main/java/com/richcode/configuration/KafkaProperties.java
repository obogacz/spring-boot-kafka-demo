package com.richcode.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KafkaProperties {

    public static final String KAFKA_BOOTSTRAP_SERVERS_PLACEHOLDER = "${kafka.bootstrap-servers}";
    public static final String KAFKA_CONSUMER_GROUP_ID_PLACEHOLDER = "${kafka.consumer.group-id}";
    public static final String KAFKA_TOPIC_PURCHASE_PLACEHOLDER = "${kafka.topics.purchases.name}";

    @Value(value = KAFKA_BOOTSTRAP_SERVERS_PLACEHOLDER)
    private String bootstrapAddress;

    @Value(value = KAFKA_CONSUMER_GROUP_ID_PLACEHOLDER)
    private String consumerGroupId;

    @Value(value = KAFKA_TOPIC_PURCHASE_PLACEHOLDER)
    private String topicPurchasesName;

}
