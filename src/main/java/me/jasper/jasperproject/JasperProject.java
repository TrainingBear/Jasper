package me.jasper.jasperproject;
import lombok.Getter;
import me.jasper.jasperproject.Animation.Animation;
import me.jasper.jasperproject.Animation.PaperAnimationCommand;
import me.jasper.jasperproject.Commands.CommandManager;
import me.jasper.jasperproject.Dungeon.ExecuteCommand;
import me.jasper.jasperproject.Dungeon.GeneratorCommandExecutor;
import me.jasper.jasperproject.FileConfiguration.Configurator;
import me.jasper.jasperproject.Jam.*;
import me.jasper.jasperproject.JasperEntity.EntityCommand;
import me.jasper.jasperproject.JasperEntity.MobEventListener.JSMDamagedEvent;
import me.jasper.jasperproject.JasperEntity.MobEventListener.JSMDeathEventListener;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Warper;
import me.jasper.jasperproject.JasperItem.Items;
import me.jasper.jasperproject.JasperItem.JasperItemCommand;
import me.jasper.jasperproject.Listener.*;
import me.jasper.jasperproject.TabCompleter.SummonItemDisplay;

import me.jasper.jasperproject.Util.SignGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


public final class JasperProject extends JavaPlugin {

    @Getter private static JasperProject plugin;
    @Getter private static PluginManager PM;
    @Getter private static Configurator animationConfig;

    @Override
    public void onEnable() {
        plugin = this;
        animationConfig = new Configurator(new File(plugin.getDataFolder(), "\\Animations"));
        animationConfig.load();


        Items.register();
        try {
            createDirectories();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Animation.loadCommandTabCompleter();


//        CommandManager.getInstance()
//                .register(new PaperAnimationCommand());


        PluginManager PM = Bukkit.getServer().getPluginManager();
        PM.registerEvents(new Joinmsg(this), this);
        PM.registerEvents(new InvenAhhListener(), this);
        PM.registerEvents(new PlotMenuListener(), this);

        PM.registerEvents(new JSMDeathEventListener(), this);
        PM.registerEvents(new JSMDamagedEvent(this), this);

        PM.registerEvents(new Animator(), this);
        PM.registerEvents(new Teleport(), this);
        PM.registerEvents(new Warper(), this);
        PM.registerEvents(new Grappling_Hook(), this);

//        BukkitTask analog = new ClockExecutor(this).runTaskTimer(this,0,20);
//        BukkitTask detak = new ClockExecutor.Detak().runTaskTimer(this,0,40);

        /// Ini command register di pindah di Bootstrap soon,
        /// Biar lebih modern. tapi cuman support paper doang
        /// jadi jangan register command disini
        Objects.requireNonNull(this.getCommand("summondisplayi")).setTabCompleter(new SummonItemDisplay(this));
        Objects.requireNonNull(this.getCommand("summondisplayi")).setExecutor(new SummonItemDisplay(this));

        Objects.requireNonNull(Objects.requireNonNull(this.getCommand("dungeon"))).setTabCompleter(new GeneratorCommandExecutor(this));
        Objects.requireNonNull(this.getCommand("dungeon")).setExecutor(new GeneratorCommandExecutor(this));

        this.getCommand("test").setExecutor(new ExecuteCommand(this));
        this.getCommand("Analog").setExecutor(new ClockConfigurationForCommands(this));
        this.getCommand("jmob").setExecutor(new EntityCommand());

        this.getCommand("jitem").setExecutor(new JasperItemCommand());
        this.getCommand("jitem").setTabCompleter(new JasperItemCommand());

//        this.getCommand("animate").setExecutor(new AnimationCommand());

        System.out.println("Jasper is online now!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[JasperProject] Disabling...");
        SignGUI.getInstance().destroy();
        this.getLogger().info("[JasperProject] this plugin has been disabled!");
    }

    public static void registerEvent(Listener e){
        Bukkit.getServer().getPluginManager().registerEvents(e, JasperProject.getPlugin());
    }

    private void createDirectories() throws IOException {
        Files.createDirectories(Path.of(plugin.getDataFolder().getPath() + "\\Animations"));
    }

//    private void loadAnimationsConfig(){
//        File file = new File(plugin.getDataFolder()+"\\Animations");
//        File[] configs = file.listFiles();
//        if(configs==null) return;
//        int total_config = 0;
//        for (File config_ : configs) {
//            File[] configs__ = config_.listFiles((dir, name) -> name.endsWith(".yml"));
//            if(configs__==null) return;
//            for (File config : configs__) {
//                Configurator.getFiles().add(config);
//                FileConfiguration aconfig = YamlConfiguration.loadConfiguration(config);
//                if(aconfig.getBoolean("isRunning")){
//                    String animation_name = Configurator.getFileName(config);
//                    Bukkit.getLogger().info("[JasperProject] "+animation_name+" is running!");
//                    Animation.play(animation_name, true);
//                }
//                Bukkit.getLogger().info("[JasperProject] Loaded "+config.getName());
//                total_config++;
//            }
//        }
//        Bukkit.getLogger().info("[JasperProject] Loaded "+total_config+" configs");
//    }

}
