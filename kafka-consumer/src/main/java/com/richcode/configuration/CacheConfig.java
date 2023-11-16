package com.richcode.configuration;

import com.richcode.domain.PurchaseEvent;
import org.infinispan.Cache;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.richcode.cache.OffsetCacheRepository.Offset;

@Configuration
@ConditionalOnProperty(name = "kafka.consumer.idempotent", havingValue = "true")
class CacheConfig {

    private static final String PURCHASE_EVENT_CACHE = "purchaseEventCache";
    private static final String OFFSET_CACHE = "offsetCache";
    private static final long PURCHASE_EVENT_CACHE_EXPIRATION_TIME_IN_DAYS = 1;
    private static final long OFFSET_CACHE_EXPIRATION_TIME_IN_HOURS = 12;

    private static final Class[] CACHE_SERIALIZABLE_CLASSES = {
        Offset.class,
        PurchaseEvent.class,
        String.class,
        UUID.class
    };

    @Bean
    public Cache<UUID, PurchaseEvent> purchaseEventCache() {
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
    public Cache<String, Offset> offsetCache() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder
            .clustering()
            .cacheMode(CacheMode.REPL_SYNC);

        configurationBuilder
            .expiration()
            .maxIdle(OFFSET_CACHE_EXPIRATION_TIME_IN_HOURS, TimeUnit.HOURS);

        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfiguration());
        cacheManager.defineConfiguration(OFFSET_CACHE, configurationBuilder.build());
        return cacheManager.getCache(OFFSET_CACHE);
    }

    private GlobalConfiguration globalConfiguration() {
        GlobalConfigurationBuilder configurationBuilder = new GlobalConfigurationBuilder();

        configurationBuilder
            .transport()
            .defaultTransport()
            .clusterName("kafka-consumer-demo-cache");

        configurationBuilder
            .serialization()
            .marshaller(new JavaSerializationMarshaller())
            .allowList()
            .addClasses(CACHE_SERIALIZABLE_CLASSES);

        return configurationBuilder.build();
    }

}
