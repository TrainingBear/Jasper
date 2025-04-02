package me.jasper.jasperproject;
import lombok.Getter;
import me.jasper.jasperproject.Animation.Animation;
import me.jasper.jasperproject.Animation.AnimationCommand;
import me.jasper.jasperproject.Dungeon.ExecuteCommand;
import me.jasper.jasperproject.FileConfiguration.ConfigDungeon;
import me.jasper.jasperproject.Dungeon.GeneratorCommandExecutor;
import me.jasper.jasperproject.FileConfiguration.Configurator;
import me.jasper.jasperproject.FileConfiguration.LaunchPadConfiguration;
import me.jasper.jasperproject.Jam.*;
import me.jasper.jasperproject.JasperEntity.EntityCommand;
import me.jasper.jasperproject.JasperEntity.MobEventListener.JSMDamagedEvent;
import me.jasper.jasperproject.JasperEntity.MobEventListener.JSMDeathEventListener;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.Items;
import me.jasper.jasperproject.JasperItem.JasperItemCommand;
import me.jasper.jasperproject.Listener.*;
import me.jasper.jasperproject.TabCompleter.SummonItemDisplay;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JasperProject extends JavaPlugin {

    @Getter private static JasperProject plugin;
    @Getter private static PluginManager PM;

    @Override
    public void onEnable() {
        plugin = this;
        Items.register();
        try {
            createDirectories();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AnimationCommand.loadTabCompleter();
        loadAnimationsConfig();

        LaunchPadConfiguration.setup();
        LaunchPadConfiguration.get().options().copyDefaults();
        LaunchPadConfiguration.save();

        ConfigDungeon.setup();
        ConfigDungeon.get().options().copyDefaults();
        ConfigDungeon.save();

        saveDefaultConfig();

        Clock.setup();
        System.out.println("Loaded Clock Config: " + Clock.get().getKeys(true));
        Clock.save();

        me.jasper.jasperproject.FileConfiguration.Test.setup();
        me.jasper.jasperproject.FileConfiguration.Test.get().options().copyDefaults(true);
        me.jasper.jasperproject.FileConfiguration.Test.save();


        PluginManager PM = Bukkit.getServer().getPluginManager();
        PM.registerEvents(new Joinmsg(this), this);
//        PM.registerEvents(new whenRainCancel(), this);
        PM.registerEvents(new InvenAhhListener(), this);
        PM.registerEvents(new PlotMenuListener(), this);

        PM.registerEvents(new JSMDeathEventListener(), this);
        PM.registerEvents(new JSMDamagedEvent(this), this);

        PM.registerEvents(new Animator(), this);
        PM.registerEvents(new Teleport(), this);
        PM.registerEvents(new Grappling_Hook(), this);

//        BukkitTask analog = new ClockExecutor(this).runTaskTimer(this,0,20);
//        BukkitTask detak = new ClockExecutor.Detak().runTaskTimer(this,0,40);


        this.getCommand("summondisplayi").setTabCompleter(new SummonItemDisplay(this));
        this.getCommand("summondisplayi").setExecutor(new SummonItemDisplay(this));

        this.getCommand("dungeon").setTabCompleter(new GeneratorCommandExecutor(this));
        this.getCommand("dungeon").setExecutor(new GeneratorCommandExecutor(this));

        this.getCommand("test").setExecutor(new ExecuteCommand(this));
        this.getCommand("Analog").setExecutor(new ClockConfigurationForCommands(this));
        this.getCommand("jmob").setExecutor(new EntityCommand());
        

        this.getCommand("jitem").setExecutor(new JasperItemCommand());
        this.getCommand("jitem").setTabCompleter(new JasperItemCommand());

        this.getCommand("animate").setExecutor(new AnimationCommand());

        System.out.println("Jasper is online now!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[JasperProject] Disabling...");
        Clock.save();
        this.getLogger().info("[JasperProject] this plugin has been disabled!");
    }

    public static void registerEvent(Listener e){
        Bukkit.getServer().getPluginManager().registerEvents(e, JasperProject.getPlugin());
    }

    private void createDirectories() throws IOException {
        Files.createDirectories(Path.of(plugin.getDataFolder().getPath() + "\\Animations"));
    }

    private void loadAnimationsConfig(){
        File file = new File(plugin.getDataFolder()+"\\Animations");
        File[] configs = file.listFiles();
        if(configs==null) return;
        int total_config = 0;
        for (File config_ : configs) {
            File[] configs__ = config_.listFiles((dir, name) -> name.endsWith(".yml"));
            if(configs__==null) return;
            for (File config : configs__) {
                Configurator.getFiles().add(config);
                FileConfiguration aconfig = YamlConfiguration.loadConfiguration(config);
                if(aconfig.getBoolean("isRunning")){
                    String animation_name = Configurator.getFileName(config);
                    Bukkit.getLogger().info("[JasperProject] "+animation_name+" is running!");
                    Animation.play(animation_name, true);
                }
                Bukkit.getLogger().info("[JasperProject] Loaded "+config.getName());
                total_config++;
            }
        }
        Bukkit.getLogger().info("[JasperProject] Loaded "+total_config+" configs");
    }

}
