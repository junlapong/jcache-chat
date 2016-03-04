package de.consol.chat.config.expirypolicy;

import javax.cache.configuration.Factory;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.expiry.ModifiedExpiryPolicy;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
public class RoomExpiryPolicyFactory implements Factory<ExpiryPolicy> {

    private static final long serialVersionUID = -2751036478164820275L;

    @Override
    public ExpiryPolicy create() {
        return new ModifiedExpiryPolicy(Duration.ONE_MINUTE);
    }
}