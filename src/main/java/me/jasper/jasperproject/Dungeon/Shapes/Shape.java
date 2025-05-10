package me.jasper.jasperproject.Dungeon.Shapes;

import me.jasper.jasperproject.Dungeon.RoomType;

import java.awt.*;
import java.util.Map;

public interface Shape {
    byte[][][] getShape();
    RoomType getType();
    Map<Integer, Integer> getRotation();
    default Point getPastePoint(int anchor, Point point){
        byte[][] bytes1 = getShape()[anchor];
        Point p = new Point(point.x, point.y);
        int length = bytes1.length;
        if(length %2==1){
            byte x = bytes1[length / 2][0];
            byte y = bytes1[length / 2][1];
            p.translate(x*32, y*32);
            return p;
        }
        byte x = bytes1[(length / 2)-1][0];
        byte y = bytes1[(length / 2)-1][1];
        p.translate((x*32)+16, (y*32)+16);
        return p;
    }
    default void rotate(int anchor){
        int def = getRotation().getOrDefault(anchor, 0);
        getRotation().put(anchor, def==360? 0 : def+90);
        for (byte[] p : getShape()[anchor]) {
            byte temp = p[0];
            p[0] = (byte) -p[1];
            p[1] = temp;
        }
    }
}
