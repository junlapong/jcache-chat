package de.consol.chat.config.eventlistener;

import de.consol.chat.util.SpringHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.cache.event.*;
import java.io.Serializable;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
public class UserEventListener implements
        CacheEntryCreatedListener<Long, String>,
        CacheEntryExpiredListener<Long, String>,
        CacheEntryRemovedListener<Long, String>,
        Serializable {

    private static final String TOPIC_USERS_JOINED = "/topic/users/joined";
    private static final String TOPIC_USERS_LEFT = "/topic/users/left";

    private static final long serialVersionUID = 2000L;

    private SimpMessagingTemplate lookupMessagingTemplate() {
        ApplicationContext context = SpringHelper.getApplicationContext();
        return context.getBean(SimpMessagingTemplate.class);
    }

    @Override
    public void onCreated(Iterable<CacheEntryEvent<? extends Long, ? extends String>> cacheEntryEvents) throws CacheEntryListenerException {
        logEvent(cacheEntryEvents);
        SimpMessagingTemplate mt = lookupMessagingTemplate();
        cacheEntryEvents.forEach(cacheEntryEvent -> mt.convertAndSend(TOPIC_USERS_JOINED, cacheEntryEvent.getValue()));
    }

    @Override
    public void onExpired(Iterable<CacheEntryEvent<? extends Long, ? extends String>> cacheEntryEvents) throws CacheEntryListenerException {
        logEvent(cacheEntryEvents);
        SimpMessagingTemplate mt = lookupMessagingTemplate();
        cacheEntryEvents.forEach(cacheEntryEvent -> mt.convertAndSend(TOPIC_USERS_LEFT, cacheEntryEvent.getValue()));
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<? extends Long, ? extends String>> cacheEntryEvents) throws CacheEntryListenerException {
        logEvent(cacheEntryEvents);
        SimpMessagingTemplate mt = lookupMessagingTemplate();
        cacheEntryEvents.forEach(cacheEntryEvent -> mt.convertAndSend(TOPIC_USERS_LEFT, cacheEntryEvent.getValue()));
    }

    private void logEvent(Iterable<CacheEntryEvent<? extends Long, ? extends String>> cacheEntryEvents) {
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
