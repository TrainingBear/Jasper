package me.jasper.jasperproject.Dungeon.MapUtil;

import org.bukkit.map.MapCursor;

import java.util.ArrayList;
import java.util.List;

public class MapCursorCollection {
    private final List<MapCursor> cursors = new ArrayList<>();

    void addCursor(MapCursor cursor){
        cursors.add(cursor);
    }
    /**
    * ADD CURSOR.
    * @param x x.
     * @param y y.
     * @param direction direction.
     * @param type The map cursor type, eg : Type.PLAYER.
     * @param visible visible.
    *
    * */
    public void addCursor(int x, int y, byte direction, MapCursor.Type type, boolean visible){
        cursors.add(new MapCursor((byte) x, (byte) y, direction, type, visible));
    }
    public void clear(){
        cursors.clear();
    }

    int size(){
        return cursors.size();
    }

}
