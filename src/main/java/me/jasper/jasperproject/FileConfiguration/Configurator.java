package me.jasper.jasperproject.FileConfiguration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * YAML CONFIGURATOR
 * */
public abstract class Configurator {
    private static final List<File > file = new ArrayList<>();
    private static final List<FileConfiguration> config = new ArrayList<>();

    public static List<File> getFiles(){
        return file;
    }
    public static List<FileConfiguration> getConfigs(){
        return config;
    }

    public static File create(File parent, String name) throws JasperConfiguratorException{
        File file = new File(parent,name+".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("[Jasper] Failed creating ("+file.getName()+")");
            }finally {
                Bukkit.getLogger().info("[Jasper] Successful creating ("+file.getName()+")");
                Configurator.file.add(file);
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                Configurator.config.add(config);
            }
            return file;
        }

        throw new JasperConfiguratorException("This file already exist!");
    }

    public static void delete(String name){
        File file = getFile(name);
        FileConfiguration config = getConfig(name);
        if(file==null) return;
        Configurator.file.remove(file);
        Configurator.config.remove(config);
        if(file.delete()){
            Bukkit.getLogger().info("[JasperProject] deleted "+name+".yml");
        }
    }

    public static @Nullable File getFile(String name){
        for (File file : file) {
            String[] words = file.getName().split("\\.");
            for (String word : words) {
                if(word.equals(name)){
                    return file;
                }
            }
        }
        return null;
    }

    public static @Nullable FileConfiguration getConfig(String name){
        for (File file : file) {
            String[] words = file.getName().split("\\.");
            for (String word : words) {
                if(word.equals(name)){
                    return YamlConfiguration.loadConfiguration(file);
                }
            }
        }
        return null;
    }

    @Deprecated
    private static void save(String name){
        try {
            getConfig(name).save(getFile(name));
        } catch (IOException e) {
            Bukkit.getLogger().info("[JasperProject] Cant save "+name);
        }
    }

    public static void save(FileConfiguration config, String toFileName){
        try {
            config.save(getFile(toFileName));
        } catch (IOException e) {
            Bukkit.getLogger().info("[JasperProject] Cant save "+toFileName);
        }
    }


    @Deprecated
    public static void reload(String name) throws IOException, InvalidConfigurationException {
        getConfig(name).load(getFile(name));
    }

    public static void saveAll(String name){
        for (String s : getConfigsFileName()) {
            save(s);
        }
    }

    public static void reloadAll(String name){
        for (String s : getConfigsFileName()) {
            save(s);
        }
    }

    public static String getFileName(File file){
        String filename = file.getName();
        int index = filename.indexOf('.');
        return filename.substring(0,index);
    }

    public static List<String> getConfigsFileName(){
        List<String> names = new ArrayList<>();
        for (File file1 : file) {
            names.add(getFileName(file1));
        }
        return names;
    }

}

