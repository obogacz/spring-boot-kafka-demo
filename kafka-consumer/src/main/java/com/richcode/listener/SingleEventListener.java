package com.richcode.listener;

import com.richcode.domain.PurchaseEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface SingleEventListener {

    void handle(final ConsumerRecord<String, PurchaseEvent> event, final Acknowledgment ack);

}
