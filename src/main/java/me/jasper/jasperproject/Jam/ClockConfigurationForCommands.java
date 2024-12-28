package me.jasper.jasperproject.Jam;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ClockConfigurationForCommands implements CommandExecutor {
    JasperProject plugin;
    public ClockConfigurationForCommands(JasperProject plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)){
            System.out.println(ChatColor.RED+"Your not a player!");
            return false;
        }
        if(strings[0].equalsIgnoreCase("set")){
            // +0.04, +1.735, +0.5
            Location clockLocation = player.getLocation();
            clockLocation.add(0, -0.5, 0);
            Clock.get().set("Clock.Location",clockLocation);
            player.sendMessage(ChatColor.GREEN+"Clock Location has been set!");
            Clock.save();
        }if(strings[0].equalsIgnoreCase("model")){
            if((strings[1].equalsIgnoreCase("add")&&(strings[2].equalsIgnoreCase("menit")))){
                HashMap<Integer, Entity> model = new ClockUtility(plugin).ModelFinder(Clock.get().getLocation("Clock.Location"),"menit");

                ArmorStand armorstand = (ArmorStand) Clock.get().getLocation("Clock.Location").getWorld().spawnEntity(
                        Clock.get().getLocation("Clock.Location"), EntityType.ARMOR_STAND
                );
                armorstand.addScoreboardTag("menit");
                armorstand.setGravity(false);
                armorstand.setInvulnerable(true);
                armorstand.setInvisible(true);
                Clock.get().set("Clock.jarum.menit.UUID_model",model.get(1).getUniqueId().toString());
                Clock.get().set("Clock.jarum.menit.UUID_armorstand",armorstand.getUniqueId().toString());
                Clock.save();
            }if((strings[1].equalsIgnoreCase("add")&&(strings[2].equalsIgnoreCase("jam")))){
                HashMap<Integer, Entity> model = new ClockUtility(plugin).ModelFinder(Clock.get().getLocation("Clock.Location"),"jam");

                ArmorStand armorstand = (ArmorStand) Clock.get().getLocation("Clock.Location").getWorld().spawnEntity(
                        Clock.get().getLocation("Clock.Location"), EntityType.ARMOR_STAND
                );
                armorstand.addScoreboardTag("jam");
                armorstand.setGravity(false);
                armorstand.setInvulnerable(true);
                armorstand.setInvisible(true);
                Clock.get().set("Clock.jarum.jam.UUID_model",model.get(1).getUniqueId().toString());
                Clock.get().set("Clock.jarum.jam.UUID_armorstand",armorstand.getUniqueId().toString());
                Clock.save();
            }
        }
        if(strings[0].equalsIgnoreCase("facing")){
            if(strings[1].equalsIgnoreCase("north")){
                Clock.get().set("Clock.facing","north");
                Clock.get().set("Clock.Location.pitch", 0);
                Clock.get().set("Clock.Location.yaw", -90);
                Clock.save();
            }if(strings[1].equalsIgnoreCase("east")){
                Clock.get().set("Clock.facing","east");
                Clock.get().set("Clock.Location.pitch", -90);
                Clock.get().set("Clock.Location.yaw", 0);
                Clock.save();
            }
        }
        return false;
    }
}
