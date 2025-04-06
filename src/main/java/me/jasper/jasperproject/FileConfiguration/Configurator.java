package me.jasper.jasperproject.FileConfiguration;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
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
public final class Configurator {
    private static Configurator instance;

    private final List<File > file = new ArrayList<>();
    @Getter private final List<Configurator > compounds = new ArrayList<>();
    @Getter public final File parent;
    private final Plugin plugin = JasperProject.getPlugin();
    @Getter@Setter private Configurator parentCompound;

    public static Configurator getInstance(){
        if(instance==null){
            instance = new Configurator();
        }
        return instance;
    }

    @Deprecated
    private Configurator(){
        parent = this.plugin.getDataFolder();
    }
    public Configurator(File parent){
        this.parent = parent;
    }


    public List<File> getFiles(){
        return file;
    }

    public void load(@Nullable Runnable run){
        File[] files = parent.listFiles();
        for (File file : files) {
            if(file.getName().endsWith(".yml")) {
                this.file.add(file);
            }
            if(file.isDirectory()){
                Configurator compound = new Configurator(file);
                compound.load(null);
                this.compounds.add(compound);
            }
        }
        if(run!=null){
            run.run();
        }
    }

    /**
     *
     * @param name name
     * @return return the created file Configurator
     * @throws JasperConfiguratorException Exception
     */
    public Configurator create(String name) throws JasperConfiguratorException{
        File file = new File(parent,name+".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().info("[Jasper] Failed creating ("+file.getName()+")");
            }finally {
                plugin.getLogger().info("[Jasper] Successful creating ("+file.getName()+") "+file.getAbsolutePath());
                this.file.add(file);
            }
            return this;
        }

        throw new JasperConfiguratorException("This file already exist!");
    }

    public void edit(String name, Editor editor) {
        File file = getFile(name);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        try{
            editor.edit(config).save(getFile(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param name name cant contain \\
     * @return return the created Compound
     */
    public @Nullable Configurator newCompound(String name){
        if(name.contains("\\")) return null;
        File file = new File(parent, "\\"+name);
        if(!file.exists()){
            Configurator compound = new Configurator(file);
            compound.setParentCompound(this);
            compounds.add(compound);
            file.mkdir();
            return compound;
        }
        return getCompound(name);
    }

    public boolean removeCompound(String name){
        Configurator compound = getCompound(name);
        return this.compounds.remove(compound);
    }

    public @Nullable Configurator getCompound(String name){
        for (Configurator compound : this.compounds) {
            if(compound.getParent().getName().equals(name)) return compound;
        }
        return null;
    }

    public @Nullable File getFile(String name){
        name += ".yml";
        for (File file : file) {
            if (file.getName().equals(name)) return file;
        }
        return null;
    }
    public @Nullable FileConfiguration getConfig(String name){
        name += ".yml";
        for (File file : file) {
            if (file.getName().equals(name)) return YamlConfiguration.loadConfiguration(file);
        }
        return null;
    }

    public void delete(String name){
        File file = getFile(name);
        if(file==null) return;

        this.file.remove(file);
        if(file.delete()){
            Bukkit.getLogger().info("[JasperProject] deleted "+name+".yml");
        }
    }

    public static String getFileName(File file){
        String filename = file.getName();
        int index = filename.indexOf('.');
        return filename.substring(0,index);
    }

    private void contoh() throws IOException {

        ///                     DECLARATION

        /// kosong doang, brti confignya ada di folder plugins/JasperProject
        /// atau lu bisa pake Configurator.getInstance() jadi gaperlu declaration kyk gini:
        Configurator test1 = new Configurator();

        /// ini buat define foldernya
        /// kalo kek gini brti confignya ada di folder plugins/JasperProject/ContohFolderlu
        Configurator test2 = new Configurator(
                new File(JasperProject.getPlugin().getDataFolder().getParent()+"//ContohFolderlu")
        );

        /**                             EDITOR* */

        test2.create("nama-configlu"); /// gini caranya buat config baru; yaya

        test2.edit("nama-configlu", config -> {
            List<String> list = new ArrayList<>();
            config.set("test", list);

            List<String> returnedList = config.getStringList("test");
            returnedList.add("new element");
            config.set("test", returnedList);

            return config;
        });

        /**                     PERCABANGAN
         * Jadi config lu bisa bercabang
         * Jadi di dalem Configurator mu ada Configurator lagi
         * atau folder baru, cara buat cabang baru gini :
         * */

        Configurator cabang1 = new Configurator(new File(plugin.getDataFolder(), "\\Cabang1"));
        cabang1.newCompound("Cabang2"); /// entar kebuat cabang2 -> plugin/Cabang1/Cabang2

        /// cara edit cabang yang lu buat:
        final Configurator cabang2 = cabang1.getCompound("Cabang2");
        cabang2.edit("a", c->c);


        ///             KALO STARTUP HARUS DI REGISTER DI MAIN CLASS! onEnable()!
        //getter
        Configurator your_config;
        // onEnable(
            your_config = new Configurator();
            your_config.load(null);
        // )

    }

}

