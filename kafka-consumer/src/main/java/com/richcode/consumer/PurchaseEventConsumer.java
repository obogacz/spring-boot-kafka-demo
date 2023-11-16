package com.richcode.consumer;


import com.richcode.domain.PurchaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class PurchaseEventConsumer implements EventConsumer {

    @Override
    public void consume(final ConsumerRecord<String, PurchaseEvent> event) {

        // processing logic

    }

}
