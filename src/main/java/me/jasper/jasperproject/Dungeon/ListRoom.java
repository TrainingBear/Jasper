package me.jasper.jasperproject.Dungeon;

import java.util.*;

public class ListRoom {
    final static LinkedList<Room> L = new LinkedList<>(List.of(
            new Room("L room","room",8,"Lshape", 'L')
    ));
    final static LinkedList<Room> TWO = new LinkedList<>(List.of(
            new Room("2x1 room","room",8,"1x2", '2')
    ));
    final static LinkedList<Room> THREE = new LinkedList<>(List.of(
            new Room("3x1 room","room",8,"1x3", '3')
    ));
    final static LinkedList<Room> FOUR = new LinkedList<>(List.of(
            new Room("4x1 room","room",8,"1x4", '4')
    ));
    final static LinkedList<Room> BOX = new LinkedList<>(List.of(
            new Room("2x2 room","room",8,"2x2", '#')
    ));
    final static LinkedList<Room> SINGLE = new LinkedList<>(List.of(
            new Room("SINGLE","room",0,"null", '1'),
            new Room("Mini Boss","MINI_BOSS",0,"yellow", '1'),
            new Room("Puzzle 1","PUZZLE",0,"puzzle", '1')
    ));
    ListRoom(){
    }

}
