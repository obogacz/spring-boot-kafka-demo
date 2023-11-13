package com.richcode.publisher;

import com.richcode.configuration.KafkaProperties;
import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
public class SyncEventPublisher implements EventPublisher {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, PurchaseEvent> kafkaTemplate;

    @Override
    public void send(final PurchaseEvent event) {
        try {
            final var sendResult = kafkaTemplate.send(producerRecord(event));
            kafkaTemplate.flush();
            sendResult.get();
            log.info("Purchase event was sent: {}", event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Sending of event was interrupted");
        } catch (KafkaException | ExecutionException e) {
            log.error("Error while purchase event sending due to: {}", e.getMessage());
        }
    }

    private ProducerRecord<String, PurchaseEvent> producerRecord(final PurchaseEvent event) {
        return new ProducerRecord<>(kafkaProperties.getTopicPurchasesName(), event.uuid().toString(), event);
    }

}
