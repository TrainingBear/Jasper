package me.jasper.jasperproject.Dungeon.Shapes;

import lombok.Getter;
import me.jasper.jasperproject.Dungeon.RoomType;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BOX_BY_BOX implements Shape {
    private final RoomType type = RoomType.BOX;
    private Map<Integer, Integer> rotation = new HashMap<>();
    private final byte[/*anchor n*/][][/*x, y*/] shape = {
            {//anchor 1
                    {0, 0},
                    {1, 0},
                    {0, 1},
                    {1, 1},
            },
            {//anchor 2
                    {-1, 0},
                    {0, 0},
                    {-1, 1},
                    {0, 1},
            },
            {//anchor 3
                    {-1, 0},
                    {0, -1},
                    {0, 0},
                    {-1, -1},
            },
            {//anchor 4
                    {1, -1},
                    {1, 0},
                    {0, -1},
                    {0, 0},
            },
    };
}
