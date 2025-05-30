package me.jasper.jasperproject.Util.FileConfiguration;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
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
    @Getter private final Map<String, Configurator > compounds = new HashMap<>();
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
    public Configurator(String name){
        this(new File(JasperProject.getPlugin().getDataFolder(), "//"+name));
    }
    public Configurator(File parent){
        this.parent = parent;
        parent.mkdirs();
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
        try {
            File[] files = parent.listFiles();
            if(files==null) return;
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
                    this.compounds.put(file.getName(), compound);
                    compound.setParentCompound(this);
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param name name
     * @return return the created file Configurator
     * @throws JasperConfiguratorException Exception
     */
    public File newConfig(String name) {
        return create(name);
    }
    public File create(String name) {
        File file = new File(parent,name+".yml");
        parent.mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().info("Failed creating ("+file.getName()+")");
            }finally {
                plugin.getLogger().info("Successful creating ("+file.getName()+") "+file.getAbsolutePath());
                this.file.add(name+".yml");
            }
            return file;
        }
        return file;
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
    public @NotNull Configurator newCompound(String name){
        if(name.contains("\\")) return null;
        File file = new File(parent, "\\"+name);
        if(!file.exists()){
            file.mkdirs();
            Configurator compound = new Configurator(file);
            compound.setParentCompound(this);
            compounds.put(name, compound);
            return compound;
        }
        return Objects.requireNonNull(getCompound(name));
    }

    public void removeCompound(String name){
        this.compounds.remove(name);
    }

    public @Nullable Configurator getCompound(String name){
        return compounds.get(name);
    }

    public @NotNull File getFile(String name) {
//        name += ".yml";
//        for (String file : this.file) {
//            if (file.equals(name)) {
//                File value = new File(parent, name);
//                try{
//                    if(value.createNewFile()){
//                        Bukkit.getLogger().warning("[Configurator] created new files! "+name);
//                    }
//                }catch (IOException e){
//                    e.printStackTrace();
//                    Bukkit.getLogger().warning("[Configurator] something went wrong when creating "+name);
//                }
//
//                return value;
//            }
//        }

        return new File(parent,name+".yml");
    }
    public @Nullable FileConfiguration getConfig(String name){
        return getConfig(name, false);
    }

    /**
     *
     * @param name name
     * @param force if force == true : create a new config if it absent;
     * @return return null if absent, else notnull if force = true
     */
    public @NotNull FileConfiguration getConfig(String name, boolean force){
        File config = getFile(name);
        if (config.getName().endsWith(".yml")) return YamlConfiguration.loadConfiguration(config);
        if(force) return YamlConfiguration.loadConfiguration(newConfig(name));
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
        /// atau lu bisa pake Configurator.getInstance()
        /// jadi gaperlu declaration kyk gini:
        Configurator mainConfig = new Configurator();

        /// bakal membuat folder baru di plugins/JasperProject/Config321
        Configurator config321 = new Configurator("Config321");

        ///                     EDITOR
    
        /// gini caranya buat config baru
        config321.newConfig("yml123");

        ///  cara ngedit yml123 yang lu buat
        config321.edit("yml123", config -> {
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

        /// entar kebuat cabang2 -> plugin/Config321/Cabang2
        config321.newCompound("Cabang2");

        /// cara edit cabang yang lu buat:
        final Configurator cabang2 = config321.getCompound("Cabang2");
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

