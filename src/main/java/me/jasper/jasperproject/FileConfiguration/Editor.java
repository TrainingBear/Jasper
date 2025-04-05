package me.jasper.jasperproject.FileConfiguration;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public interface Editor {
     FileConfiguration edit(FileConfiguration config);
}
