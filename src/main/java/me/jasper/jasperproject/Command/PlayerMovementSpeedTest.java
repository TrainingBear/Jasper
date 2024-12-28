package me.jasper.jasperproject.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerMovementSpeedTest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        if(strings.length==1){
        p.setWalkSpeed(Float.parseFloat(strings[0]));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&fYour speed is set to " +strings[0]));
        return true;
        }
        Player target = Bukkit.getPlayerExact(strings[0]);
        target.setWalkSpeed(Float.parseFloat(strings[1]));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&',"&fYour speed is set to " +strings[1]));
        return true;
    }
}
