package me.jasper.jasperproject.FileConfiguration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Test {
    private static File file;
    private static FileConfiguration customfile;

    public static void setup() {
        file = new File(Bukkit.getPluginManager().getPlugin("JasperProject").getDataFolder(), "test.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("cant make a folder");
            }
        }
        customfile = YamlConfiguration.loadConfiguration(file);
    }


    public static FileConfiguration get () {
        return customfile;
    }

    public static void save() {
        try {
            customfile.save(file);

        } catch (IOException e) {
            System.out.println("cant save a file");
        }
    }

    public static void reload(){
        customfile = YamlConfiguration.loadConfiguration(file);
    }
}
