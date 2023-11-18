package com.richcode.listener;

import com.richcode.cache.ProcessedOffsetCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
public class RebalanceListener implements ConsumerAwareRebalanceListener {

    private final ProcessedOffsetCacheRepository processedOffsetCacheRepository;

    @Override
    public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        log.info("[REBALANCING] Revoked partitions: {}", partitions);

        final HashMap<TopicPartition, OffsetAndMetadata> lastProcessedOffsets = new HashMap<>(partitions.size());

        partitions.forEach(partition -> processedOffsetCacheRepository
            .find(partition)
            .ifPresent(offset -> lastProcessedOffsets.put(partition, offset)));

        consumer.commitSync(lastProcessedOffsets);

        log.info("[REBALANCING] Sync offset commit on revoked partitions done");
    }


    @Override
    public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        log.info("[REBALANCING] Assigning partitions: {}", partitions);

        partitions.forEach(partition -> onSinglePartitionsAssigned(consumer, partition));
    }

    private void onSinglePartitionsAssigned(Consumer<?, ?> consumer, TopicPartition partition) {
        processedOffsetCacheRepository
            .find(partition)
            .ifPresent(offset -> {
                consumer.seek(partition, (offset.offset() + 1));
                log.info("[REBALANCING] Seek to topic: {}, partition: {}, offset: {}",
                    partition.topic(), partition.partition(), offset.offset() + 1);
            });
    }

}
