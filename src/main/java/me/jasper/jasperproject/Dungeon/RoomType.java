package me.jasper.jasperproject.Dungeon;

public enum RoomType {
    L_SHAPE,
    BOX,
    FOUR_X_ONE,
    THREE_X_ONE,
    TWO_X_ONE,


    PUZZLE,
    START,
    MID,
    END,
    TRAP,
    MINI_BOSS,
    END2,

    SPECIAL(PUZZLE, TRAP, MINI_BOSS,END2),
    SINGLE(SPECIAL, START, MID, END),
    TEST;

    RoomType(){}
    RoomType(RoomType... types){
    }
}
