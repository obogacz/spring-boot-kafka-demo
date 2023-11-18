package com.richcode.configuration;

import com.richcode.cache.ProcessedOffsetCacheRepository;
import com.richcode.cache.PurchaseEventCacheRepository;
import com.richcode.domain.PurchaseEvent;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.infinispan.Cache;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static com.richcode.StrategyConfiguration.ExactlyOnceConsumerStrategy;

@Configuration
@ConditionalOnBean(ExactlyOnceConsumerStrategy.class)
class CacheConfig {

    private static final String CACHE_CLUSTER_NAME = "kafka-consumer-demo-cache";
    private static final String PURCHASE_EVENT_CACHE = "purchaseEventCache";
    private static final String PROCESSED_OFFSET_CACHE = "processedOffsetCache";
    private static final long PURCHASE_EVENT_CACHE_EXPIRATION_TIME_IN_DAYS = 7;
    private static final long OFFSET_CACHE_EXPIRATION_TIME_IN_DAYS = 1;

    private static final Class[] CACHE_SERIALIZABLE_CLASSES = {
        TopicPartition.class,
        OffsetAndMetadata.class,
        PurchaseEvent.class,
        String.class,
    };

    @Bean
    public Cache<String, PurchaseEvent> purchaseEventCache() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder
            .clustering()
            .cacheMode(CacheMode.REPL_SYNC);

        configurationBuilder
            .expiration()
            .maxIdle(PURCHASE_EVENT_CACHE_EXPIRATION_TIME_IN_DAYS, TimeUnit.DAYS);

        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfiguration());
        cacheManager.defineConfiguration(PURCHASE_EVENT_CACHE, configurationBuilder.build());
        return cacheManager.getCache(PURCHASE_EVENT_CACHE);
    }

    @Bean
    public PurchaseEventCacheRepository purchaseEventCacheRepository(final Cache<String, PurchaseEvent> cache) {
        return new PurchaseEventCacheRepository(cache);
    }

    @Bean
    public Cache<TopicPartition, OffsetAndMetadata> processedOffsetCache() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder
            .clustering()
            .cacheMode(CacheMode.REPL_SYNC);

        configurationBuilder
            .expiration()
            .maxIdle(OFFSET_CACHE_EXPIRATION_TIME_IN_DAYS, TimeUnit.DAYS);

        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfiguration());
        cacheManager.defineConfiguration(PROCESSED_OFFSET_CACHE, configurationBuilder.build());
        return cacheManager.getCache(PROCESSED_OFFSET_CACHE);
    }

    @Bean
    public ProcessedOffsetCacheRepository offsetCacheRepository(final Cache<TopicPartition, OffsetAndMetadata> cache) {
        return new ProcessedOffsetCacheRepository(cache);
    }

    private GlobalConfiguration globalConfiguration() {
        GlobalConfigurationBuilder configurationBuilder = new GlobalConfigurationBuilder();

        configurationBuilder
            .transport()
            .defaultTransport()
            .clusterName(CACHE_CLUSTER_NAME);

        configurationBuilder
            .serialization()
            .marshaller(new JavaSerializationMarshaller())
            .allowList()
            .addClasses(CACHE_SERIALIZABLE_CLASSES);

        return configurationBuilder.build();
    }

}
