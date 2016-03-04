package de.consol.chat.repository;

import de.consol.chat.model.Room;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
public interface RoomRepository extends CrudRepository<Room, Long> {
    Room findByName(String name);
}
