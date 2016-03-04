package de.consol.chat.service.entryprocessor;

import de.consol.chat.model.Message;
import de.consol.chat.model.Room;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.io.Serializable;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
public class AddMessage implements EntryProcessor<Long, Room, Object>, Serializable {
    @Override
    public Object process(MutableEntry<Long, Room> entry, Object... arguments) throws EntryProcessorException {
        Room room = entry.getValue();
        room.getMessages().add((Message) arguments[0]);
        entry.setValue(room);
        return null;
    }
}
