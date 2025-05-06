package me.jasper.jasperproject.Dungeon.Rooms;

import me.jasper.jasperproject.Dungeon.Room;
import me.jasper.jasperproject.Dungeon.RoomType;

public class MAIN_ROOM {
    Room entrance = new Room("Entrance", RoomType.START,1,"entrance", 'E');
    Room fairy = new Room("Fairy",RoomType.MID,2,"fairy", 'F');
    Room blood = new Room("Blood Room",RoomType.END,3,"blood", 'B');
    Room path1 = new Room("PATH",RoomType.TEST,4,"path1",'1');
    Room path2 = new Room("PATH2",RoomType.TEST,5,"path2", '1');
}
