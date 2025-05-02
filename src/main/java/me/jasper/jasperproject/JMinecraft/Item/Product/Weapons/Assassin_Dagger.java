package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.BackStab;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Assassin_Dagger extends JItem implements Factory {
    public Assassin_Dagger(){
        super("Assassin Dagger", Material.DIAMOND_SWORD, Rarity.LEGENDARY, ItemType.SWORD,"ASSASSIN_DAGGER");
        this.getAbilities().add(new BackStab(30f,10));
        Map<Stats, Float> stats = this.getStats();
        stats.put(Stats.DAMAGE, 130f);
        stats.put(Stats.STRENGTH, 100f);
        stats.put(Stats.ATTACK_SPEED, 130f);
        stats.put(Stats.SPEED, 130f);
        stats.put(Stats.TRUE_DEFENCE, 10f);
    }
    @Override
    public JItem create(){
        return this;
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                Util.deserialize("<!i><dark_gray>This dagger has a faint blood on it")
                ,Util.deserialize("<!i><dark_gray>looks like this thing has been passed")
                ,Util.deserialize("<!i><dark_gray>many bloodies experiences")
                ,Util.deserialize("")
        );
    }
}
