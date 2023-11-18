package com.richcode.listener;

import com.richcode.configuration.KafkaAtMostOnceConsumerConfiguration;
import com.richcode.configuration.KafkaProperties;
import com.richcode.consumer.EventConsumer;
import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class AtMostOnceBatchEventListener implements BatchEventListener {

    private final EventConsumer consumer;

    @Override
    @KafkaListener(
        topics = KafkaProperties.KAFKA_TOPIC_PURCHASE_PLACEHOLDER,
        groupId = KafkaProperties.KAFKA_CONSUMER_GROUP_ID_PLACEHOLDER,
        containerFactory = KafkaAtMostOnceConsumerConfiguration.BATCH_EVENT_LISTENER_CONTAINER_FACTORY_BEAN_NAME)
    public void handle(final ConsumerRecords<String, PurchaseEvent> events, final Acknowledgment ack) {
        log.info("[RECEIVED EVENTS] count: {}, topic-partition: {}", events.count(), events.partitions());

        commitOffset(ack);
        events.forEach(this::handle);
    }

    private void handle(final ConsumerRecord<String, PurchaseEvent> event) {
        log.info("[PROCESSING EVENT] topic: {}, partition: {}, offset: {}, event: {}",
            event.topic(), event.partition(), event.offset(), event.value());

        consumer.consume(event);
    }

    private void commitOffset(final Acknowledgment ack) {
        ack.acknowledge();
        log.info("[OFFSET ACK] Acknowledged offset");
    }

}
