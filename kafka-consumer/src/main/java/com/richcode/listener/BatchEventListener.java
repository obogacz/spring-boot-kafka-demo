package com.richcode.listener;

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
public class BatchEventListener {

    private static final int HARD_COMMIT_INTERVAL = 50;

    private final EventConsumer consumer;

    @KafkaListener(
        topics = KafkaProperties.KAFKA_TOPIC_PURCHASE_PLACEHOLDER,
        groupId = KafkaProperties.KAFKA_CONSUMER_GROUP_ID_PLACEHOLDER,
        containerFactory = KafkaProperties.KAFKA_PURCHASE_BATCH_EVENT_LISTENER_CONTAINER_FACTORY_BEAN)
    public void handle(final ConsumerRecords<String, PurchaseEvent> events, final Acknowledgment ack) {
        final AtomicInteger counter = new AtomicInteger(0);
        events.forEach(event -> handle(event, ack, counter));
        commitOffset(ack);
    }

    private void handle(final ConsumerRecord<String, PurchaseEvent> event,
                        final Acknowledgment ack,
                        final AtomicInteger counter) {

        log.info("[RECEIVED EVENT] topic: {}, partition: {}, offset: {}, event: {}",
            event.topic(), event.partition(), event.offset(), event.value());

        consumer.consume(event);

        if (isCommitMoment(counter)) {
            commitOffset(ack);
        }
    }

    private boolean isCommitMoment(final AtomicInteger counter) {
        return counter.incrementAndGet() % HARD_COMMIT_INTERVAL == 0;
    }

    private void commitOffset(final Acknowledgment ack) {
        ack.acknowledge();
    }
}
