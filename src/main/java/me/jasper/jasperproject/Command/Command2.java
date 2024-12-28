package me.jasper.jasperproject.Command;

import me.jasper.jasperproject.FileConfiguration.LaunchPadConfiguration;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Command2 implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if(commandSender instanceof Player){

            Player p = (Player) commandSender;
            if(strings.length==0){
                p.sendMessage("Hey input some arguments");
            }else {
                StringBuilder bulider = new StringBuilder();
                for (int i = 0; i < strings.length; i++) {
                    bulider.append(strings[i]);
                    bulider.append(" ");
                }
                String finalmsg;
                finalmsg = bulider.toString();
                p.sendMessage("Soo this is what u said : "+finalmsg);
            }
        }

        return true;
        }
        public static class Sapa implements CommandExecutor {
            public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
                if (commandSender instanceof Player){
                    Player p = (Player) commandSender;
                    if(strings.length==0){
                        p.sendMessage("Kamu telah menyapa dirimu sendiri, so HI");
                    }else {
                        String peler = strings[0];
                        Player target = Bukkit.getPlayerExact(peler);
                        if(target == null){
                            p.sendMessage("This player its not online!");
                        }else{
                            target.sendMessage("Kamu di sapa oleh "+ p.getName());
                        }
                    }
                }
                return true;
            }
    }

    public static class Flyy implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Please run this command as a player!");
                return true;
            }

            if (!player.hasPermission("jasperproject.fly")) {
                player.sendMessage("No permission!");
                return true;
            }

            if (args.length < 1) {
                toggleFlight(player);
                return true;
            }

            for (int i = 0; i < args.length; i++) {
                Player target = Bukkit.getPlayer(args[i]);
                if (target == null) {
                    String starget = ChatColor.RED.BOLD+args[i];
                    player.sendMessage(starget+ChatColor.RED+" its not online right now!");
                    continue;
                }

                if (toggleFlight(target)) {
                    player.sendMessage(ChatColor.LIGHT_PURPLE+"You enabled flight for " + target.getName() + "!");
                    continue;
                }

                player.sendMessage(ChatColor.LIGHT_PURPLE+"You disabled flight for " + target.getName() + "!");
            }

            return true;
        }

        private boolean toggleFlight(Player player) {
            if (player.getAllowFlight()) {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.sendMessage(ChatColor.GOLD+"Flight disabled!");
                return false;
            }

            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(ChatColor.GOLD+"Flight enabled!");
            return true;
        }

    }







    public static class Vanishh implements CommandExecutor{
                List<Player> vanishlist = new ArrayList<>();


        JasperProject plugin;
        public Vanishh(JasperProject plugin){
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if(!(commandSender instanceof Player p)){
                commandSender.sendMessage(ChatColor.RED+"Only player allowed to run this command!");
                return true;
            }

            if(!(commandSender.hasPermission("jasperproject.ngilang"))){
                commandSender.sendMessage("You don't have permission");
                return true;
            }
            int radius = 1;
            final double pi = 3.14;
            final double[] index = {0};
            final double[] y = {-1.5};
            final int[] c = {0};

            if(!vanishlist.contains(p)){
                World world = p.getWorld();
                world.playSound(p.getEyeLocation().add(0, 0, 0), Sound.BLOCK_PORTAL_TRIGGER, 1f, 1.5f);
                world.spawnParticle(Particle.PORTAL, p.getEyeLocation().add(0, -1.65, 0), 30, 0, 0, 0, 3);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        double x = Math.sin((2 * pi * index[0]) / 20);
                        double z = Math.cos((2 * pi * index[0]) / 20);
                        world.spawnParticle(Particle.DUST, p.getEyeLocation().add(x, y[0], z), 1, new Particle.DustOptions(Color.fromRGB(c[0], c[0], c[0]), 2.5f));
                        world.spawnParticle(Particle.DUST, p.getEyeLocation().add(-(x), y[0], -(z)), 1, new Particle.DustOptions(Color.fromRGB(c[0], c[0], c[0]), 2.5f));
                        c[0] = c[0] + 23;
                        y[0] = y[0] + 0.2;
                        index[0]++;
                        if (index[0] == 10) {
                            world.spawnParticle(Particle.EXPLOSION, p.getEyeLocation().add(0, (-1.50)+0.65, 0), 3, 0, 0.1, 0, 1.5);
                            toggleVanish(p);
                            world.spawnParticle(Particle.TOTEM_OF_UNDYING, p.getEyeLocation().add(0, (-1.5)+0.65, 0), 20, 0, 0, 0, 0.45);
                            world.spawnParticle(Particle.FLASH, p.getEyeLocation().add(0, (-1.5)+0.65, 0), 1, 0, 0, 0, 0);
                            world.playSound(p.getEyeLocation().add(0, 0, 0), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1f, 1.9f);
                            this.cancel();
                        }
                    }
                }.runTaskTimerAsynchronously(plugin, 0, 6);
                return true;
            }
                World world = p.getWorld();
                world.spawnParticle(Particle.DUST, p.getEyeLocation().add(0, (-1.5) + 0.8, 0), 30, 0.325, 0.6, 0.325,0, new Particle.DustOptions(Color.GRAY, 2f));
                world.spawnParticle(Particle.EXPLOSION, p.getEyeLocation().add(0, (-1.5) + 0.75, 0), 3, 0, 0.1, 0, 1.5);
                world.spawnParticle(Particle.WITCH, p.getEyeLocation().add(0, (-1.5) + 0.75, 0), 20, 0.4, 0.6, 0.4, 0);
                world.playSound(p.getEyeLocation().add(0, -1.5, 0), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1f, 1.1f);
                toggleVanish(p);


            return true;
        }
            public boolean toggleVanish(Player player){


                if(vanishlist.contains(player)){
                    vanishlist.remove(player);
                    for (Player people : Bukkit.getOnlinePlayers()){
                        people.showPlayer(plugin, player);
                    }
                    player.sendMessage(ChatColor.LIGHT_PURPLE+"U no longer ngilang anymore!");
                    return false;
                }
                vanishlist.add(player);
                for (Player people : Bukkit.getOnlinePlayers()){
                    if(people.hasPermission("jasperproject.liat_setan")){
                        people.sendMessage("Anjay kamu punya ability untuk melihat setan");
                        continue;
                    }
                    people.hidePlayer(plugin, player);
                }
                player.sendMessage(ChatColor.LIGHT_PURPLE+"Sekarang kamu ngilang!");
                return true;
            }
    }
    public static class Reload implements CommandExecutor{

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            Player p = (Player) commandSender;
            LaunchPadConfiguration.save();
            p.sendMessage(ChatColor.RED+"Launch pad is reloading config..");
            p.sendMessage(ChatColor.GREEN+"Reload completed!");
            return true;
        }
    }

}
