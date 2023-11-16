package com.richcode.consumer;

import com.richcode.domain.PurchaseEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface EventConsumer {

    void consume(ConsumerRecord<String, PurchaseEvent> event);

}
