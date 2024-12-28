package me.jasper.jasperproject.Command;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetDay implements CommandExecutor {
    JasperProject plugin;

    public SetDay(JasperProject plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        plugin.getConfig().set("JasperTime.day", Integer.parseInt(strings[0]) );
        plugin.saveConfig();
        commandSender.sendMessage(strings[0]);

        return true;
    }
    public static class SetMonth implements CommandExecutor{
        JasperProject plugin;

        public SetMonth(JasperProject plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            plugin.getConfig().set("JasperTime.month",Integer.parseInt(strings[0]));
            plugin.saveConfig();
            commandSender.sendMessage(strings[0]);
            return true;
        }
    }


    public static class SetYear implements CommandExecutor{
        JasperProject plugin;

        public SetYear(JasperProject plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            plugin.getConfig().set("JasperTime.year",Integer.parseInt(strings[0]));
            plugin.saveConfig();
            commandSender.sendMessage(strings[0]);
            return true;
        }
    }



    public static class CheckDay implements CommandExecutor{
        JasperProject plugin;

        public CheckDay(JasperProject plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            commandSender.sendMessage(ChatColor.GRAY+""+plugin.getConfig().getInt("JasperTime.day")+"/"+plugin.getConfig().getInt("JasperTime.month")+"/"+plugin.getConfig().getInt("JasperTime.year"));
            commandSender.sendMessage(ChatColor.GRAY+""+plugin.getConfig().getInt("JasperTime.jam"));


    return true;}




}
    public static class SetHour implements CommandExecutor{
        JasperProject plugin;

        public SetHour(JasperProject plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        plugin.getConfig().set("JasperTime.jam",Integer.parseInt(strings[0]));

        if(Integer.parseInt(strings[0])>=6 || Integer.parseInt(strings[0])<=24 ){
        Bukkit.getWorld("spawn").setTime((long) ((Long.parseLong(strings[0])*1000)-6000));
        }
        if(Integer.parseInt(strings[0])<6){
        Bukkit.getWorld("spawn").setTime((long) ((Long.parseLong(strings[0])*1000)+18000));
        }
        if(Integer.parseInt(strings[0])>24){
            commandSender.sendMessage(ChatColor.RED+"jam di atas 24 itu udah gaada bego, lu bisa lanjut lagi ke jam 1-24");
        }
        commandSender.sendMessage(strings[0]);
            return true;
        }


    }

    public static class CheckMonth implements CommandExecutor{
        JasperProject plugin;

        public CheckMonth(JasperProject plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

            return true;
        }


    }

}
