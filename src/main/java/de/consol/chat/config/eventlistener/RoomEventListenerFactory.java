package de.consol.chat.config.eventlistener;

import javax.cache.configuration.Factory;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
public class RoomEventListenerFactory implements Factory<RoomEventListener> {
    @Override
    public RoomEventListener create() {
        return new RoomEventListener();
    }
}
