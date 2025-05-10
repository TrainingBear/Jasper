package me.jasper.jasperproject.Dungeon.Shapes;

import lombok.Getter;
import me.jasper.jasperproject.Dungeon.RoomType;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TOW_BY_TWO implements Shape {
    private final RoomType type = RoomType.TWO_X_ONE;
    private Map<Integer, Integer> rotation = new HashMap<>();
    private final byte[/*anchor n*/][][/*x, y*/] shape = {
            {//anchor 1
               {0, 0},
               {1, 0},
            },
            {//anchor 2
                    {-1, 0},
                    {0, 0},
            },
    };
}
