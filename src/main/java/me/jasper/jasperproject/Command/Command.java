package me.jasper.jasperproject.Command;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Please run this command as a player!");
            return true;
        }

        if (!player.hasPermission("jasperproject.fly")) {
            player.sendMessage(ChatColor.RED+"Sorry, you have no permission!");
            return true;
        }

        if (args.length < 1) {
            toggleFlight(player);
            return true;
        }else{

        for (int i = 0; i < args.length; i++) {
            Player target = Bukkit.getPlayer(args[i]);
            if (target == null) {
                String starget = args[i];
                player.sendMessage(ChatColor.BLUE.BOLD+starget+ChatColor.RED+" its not online right now!");
                continue;
            }

            if (toggleFlight(target)==false) {
                player.sendMessage("You enabled flight for " + target.getName() + "!");
                continue;
            }
            player.sendMessage("You disabled flight for " + target.getName() + "!");



        }
        }


        return true;
    }

    private boolean toggleFlight(Player player) {
        if (player.getAllowFlight()==true) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(ChatColor.GOLD + "Flying disabled!");
            return false;
        }else{

        player.setAllowFlight(true);
        player.setFlying(true);
        player.sendMessage("Flight enabled!");

        return true;
        }


    }

    public static class JoinListener implements Listener {
        JasperProject plugin;
        public JoinListener(JasperProject plugin){
            this.plugin = plugin;
        }
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e){
            Player p = e.getPlayer();
            if(p.hasPermission("jasperproject.liat_setan")){
                p.sendMessage("Anjay kamu punya ability untuk melihat setan");
            }else{
                p.hidePlayer(plugin,p);
            }
        }
    }
}