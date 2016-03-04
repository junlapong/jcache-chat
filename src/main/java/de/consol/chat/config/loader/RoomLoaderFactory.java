package de.consol.chat.config.loader;

import de.consol.chat.model.Room;
import de.consol.chat.repository.RoomRepository;
import de.consol.chat.util.SpringHelper;
import org.springframework.context.ApplicationContext;

import javax.cache.configuration.Factory;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RoomLoaderFactory implements Factory<CacheLoader<String, Room>>, Serializable {

    private static final long serialVersionUID = 2460805209031128663L;

    private RoomRepository lookupRoomRepository() {
        ApplicationContext context = SpringHelper.getApplicationContext();
        return context.getBean(RoomRepository.class);
    }

    @Override
    public CacheLoader<String, Room> create() {
        return new CacheLoader<String, Room>() {
            @Override
            public Room load(String key) throws CacheLoaderException {
                return lookupRoomRepository().findByName(key);
            }

            @Override
            public Map<String, Room> loadAll(Iterable<? extends String> keys) throws CacheLoaderException {
                Map<String, Room> users = new HashMap<>();
                keys.forEach(key -> users.put(key, lookupRoomRepository().findByName(key)));
                return users;
            }
        };
    }

}
