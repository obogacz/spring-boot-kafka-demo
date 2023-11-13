package com.richcode.publisher;

import com.richcode.configuration.KafkaProperties;
import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public class AsyncEventPublisher implements EventPublisher {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, PurchaseEvent> kafkaTemplate;

    @Override
    public void send(final PurchaseEvent event) {
        try {
            kafkaTemplate.send(producerRecord(event));
            log.info("Purchase event was sent: {}", event);
        } catch (KafkaException e) {
            log.error("Error while purchase event sending due to: {}", e.getMessage());
        }
    }

    private ProducerRecord<String, PurchaseEvent> producerRecord(final PurchaseEvent event) {
        return new ProducerRecord<>(kafkaProperties.getTopicPurchasesName(), event.uuid().toString(), event);
    }

}
