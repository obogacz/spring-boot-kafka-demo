package com.richcode.listener;

import com.richcode.configuration.KafkaProperties;
import com.richcode.consumer.EventConsumer;
import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

@Slf4j
@RequiredArgsConstructor
public class SingleEventListener {

    private final EventConsumer consumer;

    @KafkaListener(
        topics = KafkaProperties.KAFKA_TOPIC_PURCHASE_PLACEHOLDER,
        groupId = KafkaProperties.KAFKA_CONSUMER_GROUP_ID_PLACEHOLDER,
        containerFactory = KafkaProperties.KAFKA_PURCHASE_LISTENER_CONTAINER_FACTORY_BEAN)
    public void handle(PurchaseEvent event,
                       Acknowledgment ack,
                       @Header(KafkaHeaders.RECEIVED_KEY) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.OFFSET) Long offset) {

        log.info("[RECEIVED EVENT] topic: {}, partition: {}, offset: {}, event: {}", topic, partition, offset, event);

        consumer.consume(event);

        ack.acknowledge();
    }

}
