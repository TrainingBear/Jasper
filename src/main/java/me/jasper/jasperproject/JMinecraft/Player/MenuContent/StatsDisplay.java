package me.jasper.jasperproject.JMinecraft.Player.MenuContent;

import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StatsDisplay extends Border {
    public StatsDisplay(int ID, Player player) {
        super(ID, Material.REDSTONE, true, Util.deserialize("<!i>Your stats"), false);
        getItem().editMeta(e->{
            e.lore(Stats.toLore(Stats.fromPlayer(player)));
        });
    }
}
