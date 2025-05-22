package me.jasper.jasperproject.Dungeon.Floors;

import me.jasper.jasperproject.Dungeon.*;
import me.jasper.jasperproject.JMinecraft.Player.PlayerGroup;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.FileConfiguration.Configurator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class FloorONE extends Dungeon{
    public FloorONE(PlayerGroup group) {
        super(group, 3, 3);
    }
    @Override
    public void initialize(DungeonHandler handler) {
        Configurator config = JasperProject.getDungeonConfig();
        Configurator compound = config.getCompound("TEST");
        FileConfiguration floorOne = compound.getConfig("TEST");
        for (RoomType value : RoomType.values()) {
            if(floorOne.contains(value.name())){
                for (String s : floorOne.getStringList(value.name())) {
                    String schema_path = config.getParent().getPath() + "//rooms//" + value.name() + "//" + s + ".schem";
                    if(RoomType.isSpecial(value))
                        handler.addRoom(RoomType.SPECIAL, new Room(s, value, schema_path));
                    else handler.addRoom(value, new Room(s, value, schema_path));
                    Bukkit.broadcastMessage("Initialized "+s+" as "+value.name()+" at "+schema_path);
                }
            }
        }
    }
}
