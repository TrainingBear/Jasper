package me.jasper.jasperproject.Command;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Test implements CommandExecutor {
    private final JasperProject plugin;
    public Test(JasperProject plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            String np = p.getName()+".";
            if(strings[0].equalsIgnoreCase("tostring")){
                p.sendMessage(plugin.getConfig().getLocation(np).toString());
            }else if(strings.length==0){
                p.sendMessage("input some arguments idiot! >:(");
            }
            else{
                p.sendMessage("input an argumen of tostring pelease!!! >>>:(((");
            }
        }

        return true;
    }
}
