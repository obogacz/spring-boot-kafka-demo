package com.richcode.cache;

import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.infinispan.Cache;

import java.util.Optional;

@RequiredArgsConstructor
public class ProcessedOffsetCacheRepository {

    private final Cache<String, OffsetAndMetadata> cache;

    public void save(ConsumerRecord<String, PurchaseEvent> event) {
        save(event.topic(), event.partition(), event.offset());
    }

    public void save(String topic, int partition, long offset) {
        cache.put(key(topic, partition), new OffsetAndMetadata(offset));
    }

    public Optional<OffsetAndMetadata> find(TopicPartition partition) {
        return Optional.ofNullable(cache.get(key(partition)));
    }

    private String key(TopicPartition partition) {
        return key(partition.topic(), partition.partition());
    }

    private String key(String topic, int partition) {
        return topic + "-" + partition;
    }

}
