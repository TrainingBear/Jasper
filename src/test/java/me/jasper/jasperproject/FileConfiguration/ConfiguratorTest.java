package me.jasper.jasperproject.FileConfiguration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
class ConfiguratorTest {

    Configurator config = new Configurator(new File("C:\\Users\\user\\AppData\\Roaming\\.feather\\player-server\\servers\\7a1e3607-139e-4341-a6b9-6340739908da\\plugins\\JasperProject\\Animations"));
    @Test
    void getConfig() {
        config.load(e -> {
//            if(e.getName().endsWith(".yml")) System.out.println(e.getName()+" loaded");
        });
        File file = config.getCompound("a").getFile("a");
        FileConfiguration config = this.config.getCompound("a").getConfig("a");
        System.out.println(config.get("isRunning"));
    }

}