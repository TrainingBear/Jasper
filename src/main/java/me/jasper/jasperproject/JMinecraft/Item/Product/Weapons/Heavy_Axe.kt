package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Bash;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Heavy_Axe extends JItem implements Factory {
    @Getter private final static Heavy_Axe instance = new Heavy_Axe();
    public Heavy_Axe(){
        super("Heavy Axe", Material.DIAMOND_AXE, Rarity.EPIC, ItemType.AXE,  "HEAVY_AXE");
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DAMAGE,120f);
        stats.put(Stats.STRENGTH,93f);
        stats.put(Stats.CRIT_DAMAGE,43f);
        stats.put(Stats.CRIT_CHANCE,30f);
        this.getAbilities().add(new Bash(5,0f));
    }
    @Override
    public JItem create() {
        return this;
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                Util.deserialize("<!i>kapak terberat sedunia, lebih berat dari beban anak <dark_gray>")
        );
    }
}
