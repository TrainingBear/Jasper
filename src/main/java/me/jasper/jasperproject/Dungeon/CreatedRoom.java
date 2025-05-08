package me.jasper.jasperproject.Dungeon;

public class CreatedRoom {
    public final static Room entrance = new Room("Entrance", RoomType.START,1,"entrance", 'E');
    public final static Room fairy = new Room("Fairy",RoomType.MID,2,"fairy", 'F');
    public final static Room blood = new Room("Blood Room",RoomType.END,3,"blood", 'B');
    public final static Room path1 = new Room("PATH",RoomType.TEST,4,"path1",'1');
    public final static Room path2 = new Room("PATH2",RoomType.TEST,5,"path2", '1');
    public static final Room L = new Room("L room",RoomType.L_SHAPE,8,"Lshape", 'L');
    public static final Room BOX = new Room("2x2 room",RoomType.BOX,8,"2x2", '#');
    public final static Room SINGLE = new Room("SINGLE",RoomType.SINGLE,0,"null", '1');
    public final static Room TWO =new Room("2x1 room",RoomType.TWO_X_ONE,8,"1x2", '2');
    public final static Room THREE = new Room("3x1 room",RoomType.THREE_X_ONE,8,"1x3", '3');
    public final static Room FOUR = new Room("4x1 room",RoomType.FOUR_X_ONE,8,"1x4", '4');
    public final static Room PUZZLE1 = new Room("Puzzle 1",RoomType.PUZZLE,0,"puzzle", 'S');
    public final static Room MINI_BOSS = new Room("Mini Boss",RoomType.MINI_BOSS,0,"yellow", 'S');
    public final static Room TRAP = new Room("Trap",RoomType.TRAP,0,"trap", 'S');
}
