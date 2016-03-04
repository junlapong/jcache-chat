package de.consol.chat.config.eventlistener;

import de.consol.chat.model.Room;
import de.consol.chat.util.SpringHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.cache.event.*;
import java.io.Serializable;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
public class RoomEventListener implements
        CacheEntryCreatedListener<Long, Room>,
        CacheEntryUpdatedListener<Long, Room>,
        CacheEntryExpiredListener<Long, Room>,
        CacheEntryRemovedListener<Long, Room>,
        Serializable {

    private static final String TOPIC_ROOMS_CREATED = "/topic/rooms/created";
    private static final String TOPIC_ROOMS_REMOVED = "/topic/rooms/removed";
    private static final String TOPIC_MESSAGES = "/topic/messages";

    private static final long serialVersionUID = 1000L;

    private SimpMessagingTemplate lookupMessagingTemplate() {
        ApplicationContext context = SpringHelper.getApplicationContext();
        return context.getBean(SimpMessagingTemplate.class);
    }

    @Override
    public void onCreated(Iterable<CacheEntryEvent<? extends Long, ? extends Room>> cacheEntryEvents) throws CacheEntryListenerException {
        logEvent(cacheEntryEvents);
        SimpMessagingTemplate mt = lookupMessagingTemplate();
        cacheEntryEvents.forEach(cacheEntryEvent -> mt.convertAndSend(TOPIC_ROOMS_CREATED, cacheEntryEvent.getValue()));
    }

    @Override
    public void onUpdated(Iterable<CacheEntryEvent<? extends Long, ? extends Room>> cacheEntryEvents) throws CacheEntryListenerException {
        logEvent(cacheEntryEvents);
        SimpMessagingTemplate mt = lookupMessagingTemplate();
        cacheEntryEvents.forEach(cacheEntryEvent -> mt.convertAndSend(TOPIC_MESSAGES, cacheEntryEvent.getValue().getLastMessage()));
    }

    @Override
    public void onExpired(Iterable<CacheEntryEvent<? extends Long, ? extends Room>> cacheEntryEvents) throws CacheEntryListenerException {
        logEvent(cacheEntryEvents);
        SimpMessagingTemplate mt = lookupMessagingTemplate();
        cacheEntryEvents.forEach(cacheEntryEvent -> mt.convertAndSend(TOPIC_ROOMS_REMOVED, cacheEntryEvent.getValue()));
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<? extends Long, ? extends Room>> cacheEntryEvents) throws CacheEntryListenerException {
        logEvent(cacheEntryEvents);
        SimpMessagingTemplate mt = lookupMessagingTemplate();
        cacheEntryEvents.forEach(cacheEntryEvent -> mt.convertAndSend(TOPIC_ROOMS_REMOVED, cacheEntryEvent.getValue()));
    }

    private void logEvent(Iterable<CacheEntryEvent<? extends Long, ? extends Room>> cacheEntryEvents) {
        cacheEntryEvents.forEach(event ->
                System.out.println(
                        String.format("[%s][%s] - %s (%s)(%s)",
                                event.getSource().getName(),
                                event.getEventType(),
                                event.getValue(),
                                event.getOldValue(),
                                this
                        )
                )
        );
    }
}
