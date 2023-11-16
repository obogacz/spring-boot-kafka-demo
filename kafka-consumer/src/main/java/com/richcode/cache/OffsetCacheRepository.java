package com.richcode.cache;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.infinispan.Cache;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@RequiredArgsConstructor
public class OffsetCacheRepository {

    private final Cache<String, Offset> cache;

    public void save(String topic, int partition, long offset) {
        cache.put(key(topic, partition), new Offset(offset));
    }

    public Offset find(String topic, int partition) {
        return cache.get(key(topic, partition));
    }

    private static String key(String topic, int partition) {
        return topic + "-" + partition;
    }

    public record Offset(long offset) implements Serializable {

        public OffsetAndMetadata toOffsetAndMetadata() {
            return new OffsetAndMetadata(offset);
        }

    }
}
