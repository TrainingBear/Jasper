package me.jasper.jasperproject.Command;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnAhhPoint implements CommandExecutor {
    private final JasperProject plugin;

    public SpawnAhhPoint(JasperProject plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            if(strings.length==0){
                p.sendMessage("Please input a name of spawn point");
            }
            if(strings.length==1){
                String np = p.getName()+".";
                String spn = strings[0];
                if(strings[0].equalsIgnoreCase(plugin.getConfig().getLocation(np+strings[0]).toString())){
                    Location setloc = p.getLocation();
                    plugin.getConfig().set(np+spn, setloc);
                    plugin.saveConfig();
                    p.sendMessage("a new spawn point named "+ spn +" just being set!");
                }
                }else{
                    p.sendMessage("U cant have a spawn point with == name, please input another name");
            }
        }
        return true;
    }








    public static class Spawnn implements CommandExecutor{
        private final JasperProject plugin;
        public Spawnn(JasperProject plugin){
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                String np = p.getName()+".";
                Location loc = plugin.getConfig().getLocation(np+strings[0]);
                if(strings.length==0){
                    p.sendMessage("please input your spawn point name!");
                }
                if (strings.length==1){
                    if(strings[0].equalsIgnoreCase(plugin.getConfig().getLocation(np+strings[0]).toString())){

                        p.teleport(loc);
                        p.sendMessage("You just teleported to " +strings[0]);
                    }else{
                        p.sendMessage("Unknown spawn point name. please input your spawn point corectly!");
                    }
                }
            }
            return true;
        }
    }













    public static class ResetSpawnPoint implements CommandExecutor{
        private final JasperProject plugin;

        public ResetSpawnPoint(JasperProject plugin){
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if(commandSender instanceof Player){
                Player p = (Player) commandSender;
                plugin.getConfig().set("spawn.x", 38);
                plugin.getConfig().set("spawn.y",  229 );
                plugin.getConfig().set("spawn.z",   2);
                plugin.getConfig().set("spawn.world", "spawn");
                plugin.saveConfig();
                p.sendMessage("Your spawnpoint has been reseted to ");
            }
            return true;
        }
    }
}
