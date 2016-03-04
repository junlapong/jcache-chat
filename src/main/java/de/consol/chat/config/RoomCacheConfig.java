package de.consol.chat.config;

import de.consol.chat.model.Room;
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
public class RoomCacheConfig implements Serializable {
    public static final String ROOM_CACHE = "room";

    @Bean(name = "roomCache")
    public Cache<Long, Room> getRoomCache(JCacheCacheManager cacheManager) {
        CacheManager cm = cacheManager.getCacheManager();
        return cm.getCache(ROOM_CACHE, Long.class, Room.class);
    }
}
