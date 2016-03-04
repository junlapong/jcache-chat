package de.consol.chat.config.writer;

import de.consol.chat.model.Room;
import de.consol.chat.repository.RoomRepository;
import de.consol.chat.util.SpringHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.integration.CacheWriter;
import javax.cache.integration.CacheWriterException;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by georgi on 22/12/15.
 */
@Component
public class RoomWriterFactory implements Factory<CacheWriter<Long, Room>> {
    private static final long serialVersionUID = -6215348255006608497L;

    @Override
    public CacheWriter<Long, Room> create() {
        return new RoomCacheWriter();
    }

    public class RoomCacheWriter implements CacheWriter<Long, Room>, Serializable {

        private static final long serialVersionUID = -5775282167598840947L;

        private RoomRepository lookupRoomRepository() {
            ApplicationContext context = SpringHelper.getApplicationContext();
            return context.getBean(RoomRepository.class);
        }

        @Override
        public void write(Cache.Entry<? extends Long, ? extends Room> entry) throws CacheWriterException {
            lookupRoomRepository().save(entry.getValue());
        }

        @Override
        public void writeAll(Collection<Cache.Entry<? extends Long, ? extends Room>> entries) throws CacheWriterException {
            // we assume that all supplied users were saved successfully (normally this should be verified)
            entries.forEach(entry -> lookupRoomRepository().save(entry.getValue()));
        }

        @Override
        public void delete(Object key) throws CacheWriterException {
            if (key instanceof Long) {
                lookupRoomRepository().delete((Long) key);
            }
        }

        @Override
        public void deleteAll(Collection<?> keys) throws CacheWriterException {
            // we assume that all matching users for the supplied keys were deleted (normally this should be verified)
            keys.forEach(key -> this.delete(key));
        }
    }
}
