package me.jasper.jasperproject.Dungeon.Floors;

import me.jasper.jasperproject.Dungeon.*;
import me.jasper.jasperproject.JMinecraft.Player.PlayerGroup;

public class FloorONE extends Dungeon{
    public FloorONE(PlayerGroup group) {
        super(group, 3, 3);
    }
    @Override
    public void initialize(DungeonHandler handler) {
        handler.addRoom(RoomType.SPECIAL, CreatedRoom.TRAP.clone());
        handler.addRoom(RoomType.SPECIAL, CreatedRoom.PUZZLE1.clone());
        handler.addRoom(RoomType.SINGLE, CreatedRoom.SINGLE.clone());
        handler.addRoom(RoomType.TWO_X_ONE, CreatedRoom.TWO.clone());
        handler.addRoom(RoomType.THREE_X_ONE, CreatedRoom.THREE.clone());
        handler.addRoom(RoomType.FOUR_X_ONE, CreatedRoom.FOUR.clone());
        handler.addRoom(RoomType.L_SHAPE, CreatedRoom.L.clone());
        handler.addRoom(RoomType.BOX, CreatedRoom.BOX.clone());
    }
}
