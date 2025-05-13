package me.jasper.jasperproject.Dungeon;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;

@Getter
public final class DungeonHandler {
    @Setter private boolean mainInitialized = false;
    @Setter private boolean debug_mode;
    private final Random random;
    private final Room[][] grid;
    private final Map<Point, Point> roomMap = new HashMap<>();
    private final Map<Point, Point> doorMap = new HashMap<>();
    private final Queue<Point> edge = new LinkedList<>();
    private final Stack<Point> history = new Stack<>();
    private Point entrance;
    private Point fairy;
    private Point bloodRoom;

    public void setFairy(Point point){
        grid[point.x][point.y] = CreatedRoom.fairy.clone();
        this.fairy = point;
        grid[point.x][point.y].setLoc(point);
        grid[point.x][point.y].addBody(point);
    }
    public void setBloodRoom(Point point){
        grid[point.x][point.y] = CreatedRoom.blood.clone();
        this.bloodRoom = point;
        grid[point.x][point.y].setLoc(point);
        grid[point.x][point.y].addBody(point);
    }
    public void setEntrance(Point point){
        grid[point.x][point.y] = CreatedRoom.entrance.clone();
        this.entrance = point;
        grid[point.x][point.y].setLoc(point);
        grid[point.x][point.y].addBody(point);
    }

    @Setter private Map<RoomType, LinkedList<Room>> rooms = new HashMap<>();
    public LinkedList<Room> get(RoomType type){
        return this.rooms.get(type);
    }
    public void addRoom(Map<RoomType, LinkedList<Room>> room){
        this.rooms = room;
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

    public @Nullable Room getGrid(Point point){
        return this.grid[point.x][point.y];
    }

    public Set<Room> getRoom(){
        Set<Room> room = new HashSet<>();
        for (Room[] rooms1 : grid) {
            room.addAll(Arrays.asList(rooms1));
        }
        return room;
    }
}
