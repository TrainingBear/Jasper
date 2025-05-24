package me.jasper.jasperproject;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import me.jasper.jasperproject.Bazaar.Bazaar;
import me.jasper.jasperproject.Bazaar.util.Listener;
import me.jasper.jasperproject.Bazaar.util.BazaarDatabase;

import me.jasper.jasperproject.Dungeon.DebugCommand;
import me.jasper.jasperproject.Dungeon.ExecuteCommand;
import me.jasper.jasperproject.Dungeon.Loot.TIER_ONE_CHEST;
import me.jasper.jasperproject.JMinecraft.Entity.JMob;
import me.jasper.jasperproject.JMinecraft.Entity.MobPlayer.PlayerEntity;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.HoldEvent;
import me.jasper.jasperproject.JMinecraft.Player.Ability.Mage;
import me.jasper.jasperproject.JMinecraft.Item.Util.Charge;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JMinecraft.Player.PlayerBukkitCommand;
import me.jasper.jasperproject.Util.ContainerMenu.ContentListener;
import me.jasper.jasperproject.Util.FileConfiguration.Configurator;
import me.jasper.jasperproject.Jam.*;
import me.jasper.jasperproject.JMinecraft.Entity.EntityCommand;
import me.jasper.jasperproject.JMinecraft.Item.JasperItemCommand;
import me.jasper.jasperproject.JMinecraft.Item.Util.ItemManager;

import me.jasper.jasperproject.Util.Debug;
import me.jasper.jasperproject.Util.Listener.Joinmsg;
import me.jasper.jasperproject.Util.Listener.PlayerQuitListener;
import me.jasper.jasperproject.Util.Listener.PlotMenuListener;
import me.jasper.jasperproject.Util.Listener.ProjectileHit;
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


public final class JasperProject extends JavaPlugin {

    private static Economy econ = null;
    private static Permission perms = null;
    @Getter
    private static Chat chat = null;
    @Getter private static JasperProject plugin;
    @Getter private static PluginManager PM;
    @Getter private static Configurator animationConfig;
    @Getter private static Configurator dungeonConfig ;
    @Getter private static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        plugin = this;
        PM = Bukkit.getServer().getPluginManager();
        animationConfig = new Configurator("Animations");//animationConfig.load(Animation::loadConfig);
        dungeonConfig = new Configurator("Dungeon");dungeonConfig.load();

        protocolManager = ProtocolLibrary.getProtocolManager();
        Bazaar.init();
        if(BazaarDatabase.startConnection()){
            this.getLogger().info("Created Bazaar product table!");
        }
        BazaarDatabase.loadDB();
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
//        setupPermissions();
//        setupChat();

        ItemManager.registerAll();
        ItemManager.runUpdater();

        this.getCommand("debug").setExecutor(new Debug());
        this.getCommand("dungeon").setExecutor(new DebugCommand());
        this.getCommand("dungeon").setTabCompleter(new DebugCommand());

//        CommandManager.getInstance()
//                .register(new PaperAnimationCommand())
//                .register(new BazaarCommand(), List.of("bazaar", "bz", "pasar"))
//        ;

        PM.registerEvents(new ProjectileHit(), this);
        PM.registerEvents(new Joinmsg(this), this);
        PM.registerEvents(new PlayerQuitListener(), this);
        PM.registerEvents(new PlotMenuListener(), this);
        PM.registerEvents(new ContentListener(), this);
        PM.registerEvents(new Listener(), this);
        PM.registerEvents(new JPlayer(), this);
        PM.registerEvents(new JMob.MobListener(), this);
//        PM.registerEvents(new AutoSaveListener(), this);
        PM.registerEvents(new JMob.DamageEvent(), this);
        PM.registerEvents(new HoldEvent(), this);
        PM.registerEvents(new Charge(),this);
        PM.registerEvents(new Mage(), this);
        PM.registerEvents(new Mage.Shoot(), this);
        PM.registerEvents(TIER_ONE_CHEST.INSTANCE, this);

        /// Ini command register di pindah di Bootstrap soon,
        /// Biar lebih modern. tapi cuman support paper doang
        /// jadi jangan register command disini
        this.getCommand("test").setExecutor(new ExecuteCommand(this));
        this.getCommand("Analog").setExecutor(new ClockConfigurationForCommands(this));
        this.getCommand("jmob").setExecutor(new EntityCommand());
        this.getCommand("jitem").setExecutor(new JasperItemCommand());
        this.getCommand("jitem").setTabCompleter(new JasperItemCommand());
        this.getCommand("jplayer").setExecutor(new PlayerBukkitCommand());
        this.getCommand("jplayer").setTabCompleter(new PlayerBukkitCommand());

        protocolManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.ENTITY_STATUS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                byte b = event.getPacket().getBytes().read(0);
                if(b == 2){
                    event.setCancelled(true);
                }
            }
        });

        this.getLogger().info("Plugin Loaded!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[JasperProject] Disabling...");
        SignGUI.getInstance().destroy();
        Structure.destroyBox();
        this.getLogger().info("[JasperProject] this plugin has been disabled!");
        PlayerEntity.killall();
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
