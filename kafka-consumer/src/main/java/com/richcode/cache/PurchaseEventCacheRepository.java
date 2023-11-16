package com.richcode.cache;

import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.infinispan.Cache;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RequiredArgsConstructor
public class PurchaseEventCacheRepository {

    private final Cache<UUID, PurchaseEvent> cache;

    public void save(PurchaseEvent event) {
        cache.put(event.uuid(), event);
    }

    public boolean exists(UUID uuid) {
        return cache.containsKey(uuid);
    }

}
