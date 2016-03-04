package de.consol.chat.service;

import de.consol.chat.model.Message;
import de.consol.chat.model.Room;
import de.consol.chat.service.entryprocessor.AddMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
@Service
public class RoomService {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    IdService idService;

    @Autowired
    Cache<Long, Room> roomCache;

    public Room createRoom(Room room) {
        Long roomId = idService.getNextId("room");
        room.setId(roomId);
        if (!roomCache.putIfAbsent(roomId, room)) {
            throw new RuntimeException(String.format("The room id '%s' already exists", roomId));
        }
        assert getRoom(roomId) != null;
        return room;
    }

    public Room getRoom(Long roomId) {
        return roomCache.get(roomId);
    }

    public void addMessage(Message message) {
        addMessageToCache(message);
    }

    private void addMessageToCache(final Message message) {
        roomCache.invoke(message.getRoomId(), new AddMessage(), message);
    }

    public void removeRoom(Long roomId) {
        roomCache.remove(roomId);
    }

    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        for (Cache.Entry<Long, Room> roomEntry : roomCache) {
            rooms.add(roomEntry.getValue());
        }
        return rooms;
    }
}
