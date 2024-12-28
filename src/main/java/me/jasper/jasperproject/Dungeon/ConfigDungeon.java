package me.jasper.jasperproject.Dungeon;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigDungeon {
    private static File file;
    private static FileConfiguration dungeonconfig;
    public static void setup(){
        file = new File(Bukkit.getPluginManager().getPlugin("JasperProject").getDataFolder(), "dungeonconfig.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("cant make a folder");
            }
        }
            dungeonconfig = YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration get (){return dungeonconfig;}
    public static void save() {
        try {
            dungeonconfig.save(file);

        } catch (IOException e) {
            System.out.println("cant save a file");
        }
    }
    public static void reload(){
        dungeonconfig = YamlConfiguration.loadConfiguration(file);
    }
}
