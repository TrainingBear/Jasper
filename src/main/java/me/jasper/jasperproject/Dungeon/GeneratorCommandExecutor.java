package me.jasper.jasperproject.Dungeon;

import com.sk89q.worldedit.math.BlockVector3;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Loadschem;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GeneratorCommandExecutor implements CommandExecutor, TabCompleter {
    JasperProject plugin;

    public GeneratorCommandExecutor(JasperProject plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        if(strings[0].equalsIgnoreCase("pos1")){
            ConfigDungeon.get().set("pos.pos1",p.getLocation());
            ConfigDungeon.save();
            p.sendMessage("pos1 being set!");
        }if(strings[0].equalsIgnoreCase("pos2")){
            ConfigDungeon.get().set("pos.pos2",p.getLocation());
            ConfigDungeon.save();
        }if(strings[0].equalsIgnoreCase("tested_jangan_di_coba")){
            p.sendMessage("generating with pos1&2");
            SetTheBlock.setBlockAT(ConfigDungeon.get().getLocation("pos.pos1"),ConfigDungeon.get().getLocation("pos.pos2"));
        }if(strings[0].equalsIgnoreCase("generatesize")){
            if(!(Integer.parseInt(strings[1])>11) || !(Integer.parseInt(strings[2])>11)){
                byte panjang = Byte.parseByte(strings[1]);
                byte l = Byte.parseByte(strings[2]);
                char[][] size = Generate.getLayoutSize(panjang,l);
                new Generate(plugin).generatelayout(p, size);
                return true;
            }p.sendMessage("udah gabisa lebih dari 10 tolol bego");
        }if(strings[0].equalsIgnoreCase("clear")){
            char[][] size = Generate.getLayoutSize(Byte.parseByte(strings[1]),Byte.parseByte(strings[2]));
            Generate.setgeneratelayouttoAir(size);
        }if(strings[0].equalsIgnoreCase("generatespecialroomonly")){
            if(!(Integer.parseInt(strings[1])>11) || !(Integer.parseInt(strings[2])>11)){
                byte panjang = Byte.parseByte(strings[1]);
                byte l = Byte.parseByte(strings[2]);
                char[][] size = Generate.getLayoutSize(panjang,l);
//                new Generate(plugin).(p, (int) panjang,(int)l);
                return true;
            }p.sendMessage("udah gabisa lebih dari 10 tolol bego");
        }if(strings[0].equalsIgnoreCase("generatespecialroomonlybutwithbaselayout")){
            if(!(Integer.parseInt(strings[1])>11) || !(Integer.parseInt(strings[2])>11)){
                byte panjang = Byte.parseByte(strings[1]);
                byte l = Byte.parseByte(strings[2]);
                char[][] size = Generate.getLayoutSize(panjang,l);
                new Generate(plugin).generatelayout(p,size);
//                new Generate(plugin).generateSpecialRoom(p, (int) panjang,(int)l);
                return true;
            }p.sendMessage("udah gabisa lebih dari 10 tolol bego");
        }if(strings[0].equalsIgnoreCase("generateroom")){
            BlockVector3 blockVector3 = new BlockVector3(48,70,48);
            new Loadschem(p,0,blockVector3);
            if(!(Integer.parseInt(strings[1])>11) || !(Integer.parseInt(strings[2])>11)){
                byte panjang = Byte.parseByte(strings[1]);
                byte l = Byte.parseByte(strings[2]);
                char[][] size = Generate.getLayoutSize(panjang,l);
                new Generate(plugin).generateRoom(p, panjang,l);
                return true;
            }p.sendMessage("udah gabisa lebih dari 10 tolol bego");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> recomendation = new ArrayList<>();
//        List<String> recomendation2 = new ArrayList<>();
        if(strings.length==1){

        recomendation.add("pos1");
        recomendation.add("pos2");
        recomendation.add("tested_jangan_di_coba");
        recomendation.add("generatesize");
        recomendation.add("clear");
        recomendation.add("generatespecialroomonly");
        recomendation.add("generatespecialroomonlybutwithbaselayout");
        recomendation.add("generateroom");
        }

        return recomendation;
    }
}
