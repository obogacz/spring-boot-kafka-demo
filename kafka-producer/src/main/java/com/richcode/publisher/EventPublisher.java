package com.richcode.publisher;

import com.richcode.domain.PurchaseEvent;

public interface EventPublisher {

    void send(PurchaseEvent event);

}
