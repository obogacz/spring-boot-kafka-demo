package com.richcode.configuration;

import com.richcode.cache.ProcessedOffsetCacheRepository;
import com.richcode.cache.PurchaseEventCacheRepository;
import com.richcode.domain.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.infinispan.persistence.jdbc.common.DatabaseType;
import org.infinispan.persistence.jdbc.configuration.JdbcStringBasedStoreConfigurationBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.richcode.StrategyConfiguration.ExactlyOnceConsumerStrategy;

@Slf4j
@Configuration
@RequiredArgsConstructor
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
        UUID.class
    };

    private final DataSourceProperties dataSourceProperties;

    @Bean
    public GlobalConfiguration globalConfiguration() {
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

    @Bean
    public EmbeddedCacheManager embeddedCacheManager() {
        return new DefaultCacheManager(globalConfiguration());
    }

    @Bean
    public Cache<String, PurchaseEvent> purchaseEventCache() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder
            .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
            .expiration()
                .lifespan(PURCHASE_EVENT_CACHE_EXPIRATION_TIME_IN_DAYS, TimeUnit.DAYS);

        configurePersistence(configurationBuilder);

        EmbeddedCacheManager cacheManager = embeddedCacheManager();
        cacheManager.defineConfiguration(PURCHASE_EVENT_CACHE, configurationBuilder.build());
        return cacheManager.getCache(PURCHASE_EVENT_CACHE);
    }

    @Bean
    public PurchaseEventCacheRepository purchaseEventCacheRepository(final Cache<String, PurchaseEvent> cache) {
        return new PurchaseEventCacheRepository(cache);
    }

    @Bean
    public Cache<String, OffsetAndMetadata> processedOffsetCache() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder
            .clustering()
                .cacheMode(CacheMode.REPL_SYNC)
            .expiration()
                .lifespan(OFFSET_CACHE_EXPIRATION_TIME_IN_DAYS, TimeUnit.DAYS);

         configurePersistence(configurationBuilder);

        EmbeddedCacheManager cacheManager = embeddedCacheManager();
        cacheManager.defineConfiguration(PROCESSED_OFFSET_CACHE, configurationBuilder.build());
        return cacheManager.getCache(PROCESSED_OFFSET_CACHE);
    }

    @Bean
    public ProcessedOffsetCacheRepository offsetCacheRepository(final Cache<String, OffsetAndMetadata> cache) {
        return new ProcessedOffsetCacheRepository(cache);
    }

    private void configurePersistence(final ConfigurationBuilder configurationBuilder) {
        configurationBuilder
            .persistence()
            .addStore(JdbcStringBasedStoreConfigurationBuilder.class)
                .dialect(DatabaseType.POSTGRES)
                .shared(true)
                .preload(true)
                .ignoreModifications(false)
                .purgeOnStartup(false)
            .table()
                .dropOnExit(false)
                .createOnStart(true)
                .tableNamePrefix("INFINISPAN_CACHE")
                .idColumnName("ID").idColumnType("VARCHAR(256)")
                .dataColumnName("DATA").dataColumnType("BYTEA")
                .timestampColumnName("TIMESTAMP").timestampColumnType("BIGINT")
                .segmentColumnName("SEGMENT").segmentColumnType("INT")
            .connectionPool()
                .connectionUrl(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .driverClass(dataSourceProperties.getDriverClassName());
    }

}
