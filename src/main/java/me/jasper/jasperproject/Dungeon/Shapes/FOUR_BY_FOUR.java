package me.jasper.jasperproject.Dungeon.Shapes;

import lombok.Getter;
import me.jasper.jasperproject.Dungeon.RoomType;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FOUR_BY_FOUR implements Shape {
    private final RoomType type = RoomType.FOUR_X_ONE;
    private Map<Integer, Integer> rotation = new HashMap<>();
    private final byte[/*anchor n*/][][/*x, y*/] shape = {
            {//anchor 1
               {0, 0},
               {1, 0},
               {2, 0},
               {3, 0},
            },
            {//anchor 2
                    {-1, 0},
                    {0, 0},
                    {1, 0},
                    {2, 0},
            },
            {//anchor 3
                    {-2, 0},
                    {-1, 0},
                    {0, 0},
                    {1, 0},
            },
            {//anchor 4
                    {-3, 0},
                    {-2, 0},
                    {-1, 0},
                    {0, 0},
            }
    };
}
