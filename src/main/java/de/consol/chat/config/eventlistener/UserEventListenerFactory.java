package de.consol.chat.config.eventlistener;

import javax.cache.configuration.Factory;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
public class UserEventListenerFactory implements Factory<UserEventListener> {
    @Override
    public UserEventListener create() {
        return new UserEventListener();
    }
}
