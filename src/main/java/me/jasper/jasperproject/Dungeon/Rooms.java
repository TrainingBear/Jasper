package me.jasper.jasperproject.Dungeon;

import java.util.*;

public class Rooms {
    LinkedList<Room> L = new LinkedList<>(List.of(
            new Room("L room",RoomType.L_SHAPE,8,"Lshape", 'L')
    ));
    LinkedList<Room> TWO = new LinkedList<>(List.of(
            new Room("2x1 room",RoomType.TWO_X_ONE,8,"1x2", '2')
    ));
    LinkedList<Room> THREE = new LinkedList<>(List.of(
            new Room("3x1 room",RoomType.THREE_X_ONE,8,"1x3", '3')
    ));
    LinkedList<Room> FOUR = new LinkedList<>(List.of(
            new Room("4x1 room",RoomType.FOUR_X_ONE,8,"1x4", '4')
    ));
    LinkedList<Room> BOX = new LinkedList<>(List.of(
            new Room("2x2 room",RoomType.BOX,8,"2x2", '#')
    ));
    LinkedList<Room> SINGLE = new LinkedList<>(List.of(
            new Room("SINGLE",RoomType.SINGLE,0,"null", '1')
    ));
    LinkedList<Room> SPECIAL = new LinkedList<>(List.of(
            new Room("Puzzle 1",RoomType.PUZZLE,0,"puzzle", 'S'),
            new Room("Mini Boss",RoomType.MINI_BOSS,0,"yellow", 'S'),
            new Room("Trap",RoomType.TRAP,0,"trap", 'S')
    ));
    Rooms(){
    }
    void addL(Room room){
        this.L.add(room);

    }void addTWO_X_ONE(Room room){
        this.TWO.add(room);

    }void addTHREE_X_ONE(Room room){
        this.THREE.add(room);

    }void addFOUR_X_ONE(Room room){
        this.FOUR.add(room);

    }void addBOX(Room room){
        this.BOX.add(room);

    }void addSINGLE(Room room){
        this.SINGLE.add(room);

    }void addSPECIAL(Room room){
        this.SPECIAL.add(room);

    }

}
