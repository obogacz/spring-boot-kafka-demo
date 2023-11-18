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

    private final Cache<TopicPartition, OffsetAndMetadata> cache;

    public void save(ConsumerRecord<String, PurchaseEvent> event) {
        save(event.topic(), event.partition(), event.offset());
    }

    public void save(String topic, int partition, long offset) {
        cache.put(new TopicPartition(topic, partition), new OffsetAndMetadata(offset));
    }

    public void save(TopicPartition partition, OffsetAndMetadata offset) {
        cache.put(partition, offset);
    }

    public Optional<OffsetAndMetadata> find(TopicPartition partition) {
        return Optional.ofNullable(cache.get(partition));
    }

    public Optional<OffsetAndMetadata> find(String topic, int partition) {
        return Optional.ofNullable(cache.get(new TopicPartition(topic, partition)));
    }

}
