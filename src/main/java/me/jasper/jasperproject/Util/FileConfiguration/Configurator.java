package me.jasper.jasperproject.Util.FileConfiguration;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * YAML CONFIGURATOR
 * */
public final class Configurator {
    private static Configurator instance;

    @Getter private final Set<String> file = new HashSet<>();
//    private final Map<String, File> file = new HashMap<>();
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


    public Set<String> getFiles(){
        return file;
    }

    public void load(){
        this.load(null);
    }
    /**
     * Load Configuration on plugin enabled
     * @param consumer For every .yml file loaded, u can run your logic of your config.yml
     *                 after .yml file loaded or before if the file is directory/folder.
     */
    public void load(Consumer<File> consumer){
        File[] files = parent.listFiles();
        for (File file : files) {
            if(file.getName().endsWith(".yml")) {
                String name = file.getName();
                this.file.add(name);
                Bukkit.getLogger().info("[JasperProject] [Configurator] loaded "+name);
                if(consumer!=null) consumer.accept(file);
            }
            if(file.isDirectory()){
                Configurator compound = new Configurator(file);
                compound.load(consumer);
                this.compounds.add(compound);
            }
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
                this.file.add(name+".yml");
            }
            return this;
        }

        throw new JasperConfiguratorException("This file already exist!");
    }

    public void edit(String name, Consumer<FileConfiguration> editor) {
        File file = getFile(name);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        try{
            editor.accept(config); 
            config.save(file);
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

    public @Nullable File getFile(String name) {
        name += ".yml";
        for (String file : this.file) {
            if (file.equals(name)) {
                File value = new File(parent, name);
                try{
                    if(value.createNewFile()){
                        Bukkit.getLogger().warning("[JasperProject] [Configurator] created new files! "+name);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    Bukkit.getLogger().warning("[JasperProject] [Configurator] something went wrong when creating "+name);
                }

                return value;
            }
        }
        return null;
    }
    public @Nullable FileConfiguration getConfig(String name){
        File config = getFile(name);
        if(config==null) {
//            System.out.println("return 1");
            Bukkit.getLogger().warning("[JasperProject] [Configurator] Something wrong when getting "+name+" FileConfiguration class");
            return null;
        }
        for (String file : file) {
//            System.out.println("Checking "+file+". is "+file+" equal "+name+".yml: "+file.equals(name));
            if (file.equals(name+".yml")) return YamlConfiguration.loadConfiguration(config);
        }
        return null;
    }

    public void delete(String name){
        this.file.remove(name);
        if(getFile(name).delete()){
            Bukkit.getLogger().warning("[JasperProject] [Configurator] deleted "+name+".yml");
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
        cabang2.edit("a", c->{});


        ///             KALO STARTUP HARUS DI REGISTER DI MAIN CLASS! onEnable()!
        //getter
        Configurator your_config;
        // onEnable(
            your_config = new Configurator();
            your_config.load(null);
        // )

    }

}

