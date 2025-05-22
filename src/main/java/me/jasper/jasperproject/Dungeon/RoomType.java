package me.jasper.jasperproject.Dungeon;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum RoomType {
    L_SHAPE(34, 2),
    BOX(34, 3),
    FOUR_X_ONE(34, 3),
    THREE_X_ONE(34, 2),
    TWO_X_ONE(34, 1),
    SINGLE(34, 0),

    PUZZLE(24, 2),
    START(27, 2),
    MID(5, 3),
    END(4, 3),
    TRAP(15, 2),
    MINI_BOSS(4, 0),
    END2(51, 2),

    SPECIAL(24),
    TEST(30);

    private byte color = 0;
    public static final List<String> autocomplete;
    static {
        autocomplete = new ArrayList<>();
        for (RoomType value : RoomType.values()) {
            autocomplete.add(value.name());
        }
    }
    RoomType(int id){
        this.color = (byte) ((id*4)+2);
    }
    RoomType(int id, int shade){
        if(id>100 || shade > 3) return;
        this.color = (byte) ((id*4)+shade);
    }

    public static boolean isSpecial(RoomType type){
        return type.equals(PUZZLE) ||
        type.equals(TRAP) ||
        type.equals(MINI_BOSS) ||
        type.equals(TEST);
    }
}
