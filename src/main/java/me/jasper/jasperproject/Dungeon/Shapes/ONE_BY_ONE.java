package me.jasper.jasperproject.Dungeon.Shapes;

import lombok.Getter;
import me.jasper.jasperproject.Dungeon.RoomType;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ONE_BY_ONE implements Shape {
    private final RoomType type = RoomType.FOUR_X_ONE;
    private Map<Integer, Integer> rotation = new HashMap<>();
    private final byte[/*anchor n*/][][/*x, y*/] shape = {
            {//anchor 1
               {0, 0},
            },
    };
}
