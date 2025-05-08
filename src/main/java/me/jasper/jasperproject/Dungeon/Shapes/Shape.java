package me.jasper.jasperproject.Dungeon.Shapes;

public interface Shape {
    byte[][][] getShape();
    ShapeResult getResult();
    default void rotate(int anchor){
        this.rotation = rotation == 360? 0 : rotation+90;
        for (byte[] p : getShape()[anchor]) {
            byte temp = p[0];
            p[0] = (byte) -p[1];
            p[1] = temp;
        }
    }
}
