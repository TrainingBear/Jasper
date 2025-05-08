package me.jasper.jasperproject.Dungeon.Shapes;

import lombok.Getter;

@Getter
public class FOUR_BY_FOUR implements Shape {
    private int rotation;
    private ShapeResult result;
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
