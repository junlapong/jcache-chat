package de.consol.chat.service;

import de.consol.chat.service.entryprocessor.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.cache.Cache;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
@Service
public class IdService {
    @Autowired
    Cache<String, Long> idCache;

    public Long getNextId(String sequenceName) {
        return idCache.invoke(sequenceName, new Id());
    }

}
