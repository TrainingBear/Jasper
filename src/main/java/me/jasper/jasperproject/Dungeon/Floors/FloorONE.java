package me.jasper.jasperproject.Dungeon.Floors;

import me.jasper.jasperproject.Dungeon.*;
import me.jasper.jasperproject.JMinecraft.Player.PlayerGroup;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.FileConfiguration.Configurator;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloorONE extends Dungeon{
    public FloorONE(PlayerGroup group) {
        super(group, 3, 3);
    }
    @Override
    public void initialize(DungeonHandler handler) {
        Configurator config = JasperProject.getDungeonConfig();
        Configurator compound = config.getCompound("FLOOR_ONE");
        FileConfiguration floorOne = compound.getConfig("FLOOR_ONE");
        Map<RoomType, List<String>> rooms = new HashMap<>();
        for (RoomType value : RoomType.values()) {
            if(floorOne.contains(value.name())){
                rooms.computeIfAbsent(value, k -> new ArrayList<>()).addAll(floorOne.getStringList(value.name()));
                for (String s : floorOne.getStringList(value.name())) {
                    handler.addRoom(value, new Room(s, value, s+".schem"));
                }
            }
        }
    }
}
