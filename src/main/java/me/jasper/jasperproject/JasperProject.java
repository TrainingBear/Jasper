package me.jasper.jasperproject;
import lombok.Getter;
import me.jasper.jasperproject.Animation.Animation;
import me.jasper.jasperproject.Animation.PaperAnimationCommand;
import me.jasper.jasperproject.Bazaar.Bazaar;
import me.jasper.jasperproject.Bazaar.BazaarCommand;
import me.jasper.jasperproject.Bazaar.util.Listener;
import me.jasper.jasperproject.Bazaar.util.BazaarDatabase;

import me.jasper.jasperproject.Dungeon.ExecuteCommand;
import me.jasper.jasperproject.Dungeon.GeneratorCommandExecutor;
import me.jasper.jasperproject.Util.AutoSaveListener;
import me.jasper.jasperproject.Util.Commands.CommandManager;
import me.jasper.jasperproject.Util.ContainerMenu.ContentListener;
import me.jasper.jasperproject.Util.FileConfiguration.Configurator;
import me.jasper.jasperproject.Jam.*;
import me.jasper.jasperproject.JasperEntity.EntityCommand;
import me.jasper.jasperproject.JasperEntity.MobEventListener.JSMDamagedEvent;
import me.jasper.jasperproject.JasperEntity.MobEventListener.JSMDeathEventListener;
import me.jasper.jasperproject.JasperItem.JasperItemCommand;
import me.jasper.jasperproject.JasperItem.Util.ItemManager;
import me.jasper.jasperproject.Listener.*;
import me.jasper.jasperproject.TabCompleter.SummonItemDisplay;

import me.jasper.jasperproject.Util.Debug;
import me.jasper.jasperproject.Util.SignGUI;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public final class JasperProject extends JavaPlugin {

    private static Economy econ = null;
    private static Permission perms = null;
    @Getter
    private static Chat chat = null;
    @Getter private static JasperProject plugin;
    @Getter private static PluginManager PM;
    @Getter private static Configurator animationConfig;

    @Override
    public void onEnable() {
        plugin = this;
        PM = Bukkit.getServer().getPluginManager();
        animationConfig = new Configurator(new File(plugin.getDataFolder(), "\\Animations"));
//        animationConfig.load(Animation::loadConfig);

        Bazaar.init();
        try {
            if(BazaarDatabase.startConnection()){
                this.getLogger().info("Created Bazaar product table!");
            }
            try {
                BazaarDatabase.loadDB();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
//        setupChat();

        ItemManager.getInstance().registerAll();

        this.getCommand("debug").setExecutor(new Debug());


//        CommandManager.getInstance()
//                .register(new PaperAnimationCommand())
//                .register(new BazaarCommand(), List.of("bazaar", "bz", "pasar"))
//        ;

        PM.registerEvents(new ProjectileHit(), this);
        PM.registerEvents(new Joinmsg(this), this);
        PM.registerEvents(new PlotMenuListener(), this);
        PM.registerEvents(new JSMDeathEventListener(), this);
        PM.registerEvents(new JSMDamagedEvent(this), this);
        PM.registerEvents(new ContentListener(), this);
        PM.registerEvents(new Listener(), this);
//        PM.registerEvents(new AutoSaveListener(), this);

        /// Ini command register di pindah di Bootstrap soon,
        /// Biar lebih modern. tapi cuman support paper doang
        /// jadi jangan register command disini
        this.getCommand("summondisplayi").setTabCompleter(new SummonItemDisplay(this));
        this.getCommand("summondisplayi").setExecutor(new SummonItemDisplay(this));
        this.getCommand("dungeon").setTabCompleter(new GeneratorCommandExecutor(this));
        this.getCommand("dungeon").setExecutor(new GeneratorCommandExecutor(this));
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
        Structure.destroyBox();
        this.getLogger().info("[JasperProject] this plugin has been disabled!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

}
