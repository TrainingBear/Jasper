package me.jasper.jasperproject.JasperItem.Product.Weapons;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.BackStab;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Stats;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
                MiniMessage.miniMessage().deserialize("<!i><gray>This dagger has a faint blood on it")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>looks like this thing has been passed")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>many bloodies experiences")
                ,MiniMessage.miniMessage().deserialize("")
        );
    }
}
