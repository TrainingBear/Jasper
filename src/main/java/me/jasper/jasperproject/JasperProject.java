package me.jasper.jasperproject;
import com.sk89q.worldedit.math.BlockVector3;
import me.jasper.jasperproject.Command.*;
import me.jasper.jasperproject.Dungeon.DungeonUtil;
import me.jasper.jasperproject.Dungeon.ExecuteCommand;
import me.jasper.jasperproject.Dungeon.Generator;
import me.jasper.jasperproject.FileConfiguration.ConfigDungeon;
import me.jasper.jasperproject.Dungeon.GeneratorCommandExecutor;
import me.jasper.jasperproject.FileConfiguration.LaunchPadConfiguration;
import me.jasper.jasperproject.Jam.*;
import me.jasper.jasperproject.Listener.*;
import me.jasper.jasperproject.TabCompleter.SummonItemDisplay;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public final class JasperProject extends JavaPlugin {

    @Override
    public void onEnable() {
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


        this.getCommand("die").setExecutor(new Diee());
        getServer().getPluginManager().registerEvents(new Joinmsg(this), this);
        getServer().getPluginManager().registerEvents(new whenRainCancel(), this);
        getServer().getPluginManager().registerEvents(new InvenAhhListener(), this);
        getServer().getPluginManager().registerEvents(new PlotMenuListener(), this);
        getServer().getPluginManager().registerEvents(new LaunchPad(this), this);
        getServer().getPluginManager().registerEvents(new PlayerFinder.PlayerListListener(), this);
        BukkitTask analog = new ClockExecutor(this).runTaskTimer(this,0,20);
        BukkitTask detak = new ClockExecutor.Detak().runTaskTimer(this,0,40);


        this.getCommand("terbang").setExecutor(new Command2.Flyy());
        this.getCommand("setspeed").setExecutor(new PlayerMovementSpeedTest());
        this.getCommand("launchpad").setExecutor(new LaunchPad(this));
        this.getCommand("ngilang").setExecutor(new Command2.Vanishh(this));
        this.getCommand("p").setExecutor(new Command2());
        this.getCommand("sapa").setExecutor(new Command2.Sapa());
        this.getCommand("sp1").setExecutor(new SpawnAhhPoint(this));
        this.getCommand("s1").setExecutor(new SpawnAhhPoint.Spawnn(this));
        this.getCommand("r1").setExecutor(new SpawnAhhPoint.ResetSpawnPoint(this));
        this.getCommand("test").setExecutor(new Test(this));
        this.getCommand("jasper").setExecutor(new JasperItem());
        this.getCommand("plot").setExecutor(new PlotMenu());
        this.getCommand("testpart").setExecutor(new TestParticle(this));
        this.getCommand("testsound").setExecutor(new TestParticle.TestSound(this));
        this.getCommand("reload").setExecutor(new Command2.Reload());
        this.getCommand("playerfinder").setExecutor(new PlayerFinder());
        this.getCommand("setday").setExecutor(new SetDay(this));
        this.getCommand("setyear").setExecutor(new SetDay.SetYear(this));
        this.getCommand("checkday").setExecutor(new SetDay.CheckDay(this));
        this.getCommand("setmonth").setExecutor(new SetDay.SetMonth(this));
        this.getCommand("sethour").setExecutor(new SetDay.SetHour(this));
        this.getCommand("summondisplayi").setTabCompleter(new SummonItemDisplay(this));
        this.getCommand("summondisplayi").setExecutor(new SummonItemDisplay(this));

        this.getCommand("dungeon").setTabCompleter(new GeneratorCommandExecutor(this));
        this.getCommand("dungeon").setExecutor(new GeneratorCommandExecutor(this));

        this.getCommand("test").setExecutor(new ExecuteCommand());
        this.getCommand("Analog").setExecutor(new ClockConfigurationForCommands(this));

        System.out.println("Jasper is online now!");
    }

    @Override
    public void onDisable() {
        Clock.save();
        System.out.println("zzz");
    }

}
