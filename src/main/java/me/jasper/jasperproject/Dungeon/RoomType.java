package me.jasper.jasperproject.Dungeon;

import java.awt.*;

public enum RoomType {
    L_SHAPE(new Color(165, 42, 42)),
    BOX(new Color(165, 42, 42)),
    FOUR_X_ONE(new Color(165, 42, 42)),
    THREE_X_ONE(new Color(165, 42, 42)),
    TWO_X_ONE(new Color(165, 42, 42)),


    PUZZLE(Color.blue),
    START(Color.GREEN),
    MID(new Color(255, 105, 180)),
    END(Color.RED),
    TRAP(Color.ORANGE),
    MINI_BOSS(Color.YELLOW),
    END2(Color.BLACK),

    SPECIAL,
    SINGLE(new Color(165, 42, 42)),
    TEST;

    Color color;
    RoomType(){}
    RoomType(Color color){
        this.color = color;
    }
}
