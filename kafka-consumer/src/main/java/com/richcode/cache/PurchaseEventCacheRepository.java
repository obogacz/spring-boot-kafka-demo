package com.richcode.cache;

import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.infinispan.Cache;

@RequiredArgsConstructor
public class PurchaseEventCacheRepository {

    private final Cache<String, PurchaseEvent> cache;

    public void save(ConsumerRecord<String, PurchaseEvent> event) {
        cache.put(key(event), event.value());
    }

    public boolean exists(ConsumerRecord<String, PurchaseEvent> event) {
        return cache.containsKey(key(event));
    }

    private String key(ConsumerRecord<String, PurchaseEvent> event) {
        return event.topic() + "-" + event.partition() + "-" + event.offset();
    }

}
