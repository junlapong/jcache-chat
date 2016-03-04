package de.consol.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

@Configuration
public class CacheConfig {

    @Value("${cache.provider}")
    private String cacheProvider;

    @Bean(name = "springCacheManager")
    public JCacheCacheManager getSpringCacheManager(CacheManager cacheManager) {
        JCacheCacheManager jCacheCacheManager = new JCacheCacheManager(cacheManager);
        return jCacheCacheManager;
    }

    @Bean(name = "cacheManager")
    public CacheManager getCacheManager() {
        CachingProvider cachingProvider = Caching.getCachingProvider(cacheProvider);
        return cachingProvider.getCacheManager();
    }
}
