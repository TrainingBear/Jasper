package me.jasper.jasperproject.Dungeon;

public abstract class Secret {
    private int x, y, z;
    abstract int getScore();
    abstract Type getType();
    public enum Type{
        CHEST,
        PICK,
        KILL,
    }
}
