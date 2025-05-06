package me.jasper.jasperproject.Dungeon;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.Dungeon.Rooms.Rooms;

import java.awt.*;
import java.util.*;

@Getter
public final class DungeonHandler {
    @Setter private boolean debug_mode;
    private final Random random;
    private final Room[][] grid;
    private final Map<Point, Point> roomMap = new HashMap<>();
    private final Map<Point, Point> doorMap = new HashMap<>();
    private final Queue<Point> edge = new LinkedList<>();
    private final Stack<Point> history = new Stack<>();
    @Setter private Point entrance;
    @Setter private Point fairy;
    @Setter private Point bloodRoom;

    public final Map<RoomType, LinkedList<Room>> rooms = new HashMap<>();
    public LinkedList<Room> get(RoomType type){
        return this.rooms.get(type);
    }
    public void addRoom(RoomType type, Room room){
        this.rooms.computeIfAbsent(type, k-> new LinkedList<>()).add(room);
    }

    public DungeonHandler(int size, long seed){
        this(size, size, seed);
    }
    public DungeonHandler(int p, int l, long seed){
        this.grid = new Room[p][l];
        this.random = new Random(seed);
    }
}
