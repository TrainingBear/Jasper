package me.jasper.jasperproject.Dungeon;

import java.util.*;

public class Rooms {
    List<Room> L = new LinkedList<>(List.of(
            new Room("L room",RoomType.L_SHAPE,8,"Lshape", 'L')
    ));
    List<Room> TWO = new LinkedList<>(List.of(
            new Room("2x1 room",RoomType.TWO_X_ONE,8,"1x2", '2')
    ));
    List<Room> THREE = new LinkedList<>(List.of(
            new Room("3x1 room",RoomType.THREE_X_ONE,8,"1x3", '3')
    ));
    List<Room> FOUR = new LinkedList<>(List.of(
            new Room("4x1 room",RoomType.FOUR_X_ONE,8,"1x4", '4')
    ));
    List<Room> BOX = new LinkedList<>(List.of(
            new Room("2x2 room",RoomType.BOX,8,"2x2", '#')
    ));
    List<Room> SINGLE = new LinkedList<>(List.of(
            new Room("SINGLE",RoomType.SINGLE,0,"null", '1')
    ));
    List<Room> SPECIAL = new LinkedList<>(List.of(
            new Room("Puzzle 1",RoomType.PUZZLE,0,"puzzle", 'S'),
            new Room("Mini Boss",RoomType.MINI_BOSS,0,"yellow", 'S'),
            new Room("Trap",RoomType.TRAP,0,"trap", 'S')
    ));
    Rooms(){
    }

}
