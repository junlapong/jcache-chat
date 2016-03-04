package de.consol.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
@Service
public class UserService {
    @Autowired
    IdService idService;

    @Autowired
    Cache<Long, String> userCache;

    public Set<String> getRegisteredUsers() {
        Set<String> users = new HashSet<>();
        for (Cache.Entry<Long, String> userEntry : userCache) {
            users.add(userEntry.getValue());
        }
        return users;
    }

    public void registerUser(String username) {
        Long userId = idService.getNextId("user");
        userCache.put(userId, username);
    }

    public void removeUser(String username) {
        for (Cache.Entry<Long, String> userEntry : userCache) {
            if (userEntry.getValue().equalsIgnoreCase(username)) {
                userCache.remove(userEntry.getKey());
            }
        }
    }
}
