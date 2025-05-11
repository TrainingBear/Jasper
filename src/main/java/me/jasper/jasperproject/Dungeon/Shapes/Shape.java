package me.jasper.jasperproject.Dungeon.Shapes;

import me.jasper.jasperproject.Dungeon.RoomType;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface Shape {
    byte[][][] getShape();
    RoomType getType();
    Map<Integer, Integer> getRotation();
    default Point getPastePoint(int anchor){
        byte[][] bytes1 = getShape()[anchor];
        Point p = new Point();
        int lastX = 0;
        int lastY = 0;
        for (byte[] bytes : bytes1) {
            int x = bytes[0] < 0? -1 : bytes[0] > 0 ? 1 : 0;
            int y = bytes[1] < 0? -1 : bytes[1] > 0 ? 1 : 0;
            boolean dobel = x!=0 && y!=0;
            if(bytes1.length==4 && dobel) continue;
            if(dobel && lastX!=0) x = 0;
            if(dobel && lastY!=0) y =0;
            lastX = x;
            lastY = y;
            x*=16;
            y*=16;
            p.translate(x, y);
        }
        return p;
    }
    default void rotate(int anchor){
        int def = getRotation().getOrDefault(anchor, 0);
        getRotation().put(anchor, def==360? 0 : def+90);
        byte[][] bytes = getShape()[anchor];
        for (byte[] p : bytes) {
            byte temp = p[0];
            p[0] = (byte) -p[1];
            p[1] = temp;
        }
    }
    static List<Point> rotate(List<Point> points, int degree){
        for (int i = 0; i < (Math.min(degree, 360) / 90); i++) {
            for (Point point : points) {
                point.move(point.y, point.x);
            }
        }
        return points;
    }
}
