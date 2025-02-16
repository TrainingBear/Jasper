package me.jasper.jasperproject.Dungeon;

import lombok.Setter;

import java.util.LinkedList;

public class RoomHandler {
    @Setter Room room = null;
    LinkedList<Room> rooms;
    RoomHandler(LinkedList<Room> rooms){
        this.rooms = rooms;
    }
    void add(Room room){
        this.rooms.add(room);
    }
}
