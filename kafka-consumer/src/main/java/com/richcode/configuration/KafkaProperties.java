package com.richcode.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KafkaProperties {

    public static final String KAFKA_CONSUMER_GROUP_ID_PLACEHOLDER = "${kafka.consumer.group-id}";
    public static final String KAFKA_TOPIC_PURCHASE_PLACEHOLDER = "${kafka.topics.purchases.name}";

    public static final String KAFKA_PURCHASE_SINGLE_EVENT_LISTENER_CONTAINER_FACTORY_BEAN = "kafkaPurchaseSingleEventListenerContainerFactory";
    public static final String KAFKA_PURCHASE_BATCH_EVENT_LISTENER_CONTAINER_FACTORY_BEAN = "kafkaPurchaseBatchEventListenerContainerFactory";

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = KAFKA_CONSUMER_GROUP_ID_PLACEHOLDER)
    private String consumerGroupId;

    @Value(value = "${kafka.consumer.processing-type:single}")
    private String consumerProcessingType;

    @Value(value = "${kafka.consumer.idempotent:false}")
    private boolean consumerIdempotent;

    @Value(value = KAFKA_TOPIC_PURCHASE_PLACEHOLDER)
    private String topicPurchasesName;

}
