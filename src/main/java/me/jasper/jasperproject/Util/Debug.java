package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JMinecraft.Entity.MobFactory;
import me.jasper.jasperproject.JMinecraft.Entity.MobPlayer.PlayerEntity;
import me.jasper.jasperproject.JMinecraft.Entity.MobRegistry;
import me.jasper.jasperproject.JMinecraft.Entity.Mobs.DreadLord;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Debug implements CommandExecutor, TabCompleter {
    private BukkitTask bukkitTask;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        if(strings.length==1){
            if(strings[0].equals("start")){
                bukkitTask = new BukkitRunnable() {
                    int degree = 0;
                    @Override
                    public void run() {
                        Location center = player.getLocation();
                        double x = center.getX() + 3 * Math.cos(Math.toRadians(degree));
                        double y = center.getZ() + 3 * Math.sin(Math.toRadians(degree));
                        Location location = new Location(player.getWorld(), x, center.getY(), y);
                        player.getWorld().spawnParticle(Particle.FLAME, location, 10, 0, 0, 0, 0);
                        degree+=10;
                    }
                }.runTaskTimer(JasperProject.getPlugin(), 1, 1);
                return true;
            }
            if(strings[0].equals("stop")){
                bukkitTask.cancel();
                return true;
            }
            if(strings[0].equals("kill")){
                PlayerEntity.kill(player.getLocation());
                return true;
            }
            if(strings[0].equals("killall")){
                MobRegistry.getInstance().deregisterAll();
                return true;
            }
        }
        Location location = player.getLocation().clone();
        MobFactory factory = new DreadLord();
        factory.spawn(location);
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of("kill", "killall");
    }
}
