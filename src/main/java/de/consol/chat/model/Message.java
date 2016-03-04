package de.consol.chat.model;

import java.io.Serializable;

/**
 * Created by georgi on 17/12/15.
 */
public class Message implements Serializable {

    String text;
    Long roomId;
    String user;

    public Message() {
    }

    public Message(String text, Long roomId, String user) {
        this.text = text;
        this.roomId = roomId;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", roomId=" + roomId +
                ", user='" + user + '\'' +
                '}';
    }
}
