package me.jasper.jasperproject.Util;

import me.jasper.jasperproject.JMinecraft.Entity.JMob;
import me.jasper.jasperproject.JMinecraft.Entity.Mobs.Zombie;
import me.jasper.jasperproject.JMinecraft.Player.Menu;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Debug implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        Location location = player.getLocation();
        Zombie zombie = new Zombie(((CraftWorld) location.getWorld()).getHandle());
        zombie.getDelegate().spawn(location);
        return true;
    }

}
