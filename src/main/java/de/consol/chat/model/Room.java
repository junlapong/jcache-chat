package de.consol.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgi on 17/12/15.
 */
@Entity
public class Room implements Serializable {

    @Id
    @Column(unique=true)
    Long id;

    String name;

    String user;

    @Transient
    List<Message> messages = new ArrayList<>();

    public Room() {
    }

    public Room(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @JsonIgnore
    public Message getLastMessage() {
        return messages.get(messages.size() - 1);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (!id.equals(room.id)) return false;
        if (!name.equals(room.name)) return false;
        if (!user.equals(room.user)) return false;
        return messages.equals(room.messages);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + messages.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", messages=" + messages +
                '}';
    }
}
