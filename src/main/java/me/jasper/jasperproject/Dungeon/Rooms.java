package me.jasper.jasperproject.Dungeon;

import java.util.*;

public class Rooms {
    List<Room> L = new LinkedList<>(List.of(
            new Room("L room","room",8,"Lshape", 'L')
    ));
    List<Room> TWO = new LinkedList<>(List.of(
            new Room("2x1 room","room",8,"1x2", '2')
    ));
    List<Room> THREE = new LinkedList<>(List.of(
            new Room("3x1 room","room",8,"1x3", '3')
    ));
    List<Room> FOUR = new LinkedList<>(List.of(
            new Room("4x1 room","room",8,"1x4", '4')
    ));
    List<Room> BOX = new LinkedList<>(List.of(
            new Room("2x2 room","room",8,"2x2", '#')
    ));
    List<Room> SINGLE = new LinkedList<>(List.of(
            new Room("SINGLE","room",0,"null", '1')
    ));
    List<Room> SPECIAL = new LinkedList<>(List.of(
            new Room("Puzzle 1","PUZZLE",0,"puzzle", 'S'),
            new Room("Mini Boss","MINI_BOSS",0,"yellow", 'S'),
            new Room("Puzzle 2","PUZZLE",0,"trap", 'S')
    ));
    Rooms(){
    }

}
