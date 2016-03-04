package de.consol.chat.controller;

import de.consol.chat.model.Message;
import de.consol.chat.model.Room;
import de.consol.chat.service.RoomService;
import de.consol.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
@RestController
public class ChatController {

    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @RequestMapping(path = "/room", method = RequestMethod.POST)
    public void createRoom(@RequestBody Room room) {
        roomService.createRoom(room);
    }

    @RequestMapping(path = "/room", method = RequestMethod.GET)
    public List<Room> getRooms() {
        return roomService.getRooms();
    }

    @RequestMapping(path = "/room/{roomId}", method = RequestMethod.DELETE)
    public void removeRoom(@PathVariable Long roomId) {
        roomService.removeRoom(roomId);
    }

    @RequestMapping(path = "/room/{roomId}/message", method = RequestMethod.POST)
    public void createChatMessage(@PathVariable Long roomId, @RequestBody Message message) {
        roomService.addMessage(message);
    }

    @RequestMapping(path = "/users/{username}", method = RequestMethod.POST)
    public List<Room> join(@PathVariable String username) {
        userService.registerUser(username);
        return roomService.getRooms();
    }

    @RequestMapping(path = "/users/{username}", method = RequestMethod.DELETE)
    public void leave(@PathVariable String username) {
        userService.removeUser(username);
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public Set<String> users() {
        return userService.getRegisteredUsers();
    }

}
