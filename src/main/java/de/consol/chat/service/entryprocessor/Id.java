package de.consol.chat.service.entryprocessor;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.io.Serializable;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
public class Id implements EntryProcessor<String, Long, Long>, Serializable {
    @Override
    public Long process(MutableEntry<String, Long> entry, Object... arguments) throws EntryProcessorException {
        if (entry.exists()) {
            Long current = entry.getValue();
            long next = current + 1;
            entry.setValue(next);
            return next;
        } else {
            entry.setValue(0L);
            return 0L;
        }
    }
}

