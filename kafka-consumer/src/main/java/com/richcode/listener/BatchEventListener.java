package com.richcode.listener;

import com.richcode.domain.PurchaseEvent;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.support.Acknowledgment;

public interface BatchEventListener {

    void handle(final ConsumerRecords<String, PurchaseEvent> events, final Acknowledgment ack);

}
