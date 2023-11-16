package com.richcode.listener;

import com.richcode.configuration.KafkaProperties;
import com.richcode.consumer.EventConsumer;
import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

@RequiredArgsConstructor
public class BatchEventListener {

    private final EventConsumer consumer;

    @KafkaListener(
        topics = KafkaProperties.KAFKA_TOPIC_PURCHASE_PLACEHOLDER,
        groupId = KafkaProperties.KAFKA_CONSUMER_GROUP_ID_PLACEHOLDER,
        containerFactory = KafkaProperties.KAFKA_PURCHASE_LISTENER_CONTAINER_FACTORY_BEAN)
    public void handle(ConsumerRecords<String, PurchaseEvent> events, Acknowledgment ack) {
        throw new UnsupportedOperationException("Not implemented");
    }

}
