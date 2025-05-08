package me.jasper.jasperproject.Dungeon;

import java.awt.*;

public enum RoomType {
    L_SHAPE(new Color(125, 36, 4)),
    BOX(new Color(145, 29, 0)),
    FOUR_X_ONE(new Color(145, 29, 0)),
    THREE_X_ONE(new Color(145, 49, 5)),
    TWO_X_ONE(new Color(110, 52, 4)),

    PUZZLE(new Color(140, 11, 191)),
    START(new Color(7, 173, 21)),
    MID(new Color(255, 105, 180)),
    END(new Color(240, 7, 7)),
    TRAP(new Color(255, 126, 0)),
    MINI_BOSS(new Color(233, 237, 7)),
    END2(Color.BLACK),

    SPECIAL,
    SINGLE(new Color(135, 65, 7)),
    TEST;

    public final Color color;
    RoomType(){
        this.color = new Color(255, 255, 255);
    }
    RoomType(Color color){
        this.color = color;
    }
}
