package de.consol.chat.config;

import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.io.Serializable;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
@Configuration
public class IdCacheConfig implements Serializable {
    public static final String ID_CACHE = "id";

    @Bean(name = "idCache")
    public Cache<String, Long> getIdCache(JCacheCacheManager cacheManager) {
        CacheManager cm = cacheManager.getCacheManager();
        return cm.getCache(ID_CACHE, String.class, Long.class);
    }
}
