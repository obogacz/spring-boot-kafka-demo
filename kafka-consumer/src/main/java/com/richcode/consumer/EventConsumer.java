package com.richcode.consumer;

import com.richcode.domain.PurchaseEvent;

public interface EventConsumer {

    void consume(final PurchaseEvent event);

}
