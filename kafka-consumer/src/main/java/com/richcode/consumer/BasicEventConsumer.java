package com.richcode.consumer;


import com.richcode.domain.PurchaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Slf4j
public class BasicEventConsumer implements EventConsumer {

    @Override
    public void consume(final ConsumerRecord<String, PurchaseEvent> event) {

        // processing logic

        log.info("[CONSUMED EVENT] topic: {}, partition: {}, offset: {}, event: {}",
            event.topic(), event.partition(), event.offset(), event.value());
    }

}
