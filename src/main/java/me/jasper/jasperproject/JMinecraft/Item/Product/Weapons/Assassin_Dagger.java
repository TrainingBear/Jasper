package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.BackStab;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

public class Assassin_Dagger extends JItem implements Factory {
    public Assassin_Dagger(){
        super("Assassin Dagger", Material.DIAMOND_SWORD, Rarity.LEGENDARY, ItemType.SWORD,"ASSASSIN_DAGGER");
        this.getAbilities().add(new BackStab(30f,10));
        this.getStats().put(Stats.DAMAGE, 130f);
        this.getStats().put(Stats.ATTACK_SPEED, 130f);
        this.getStats().put(Stats.SPEED, 130f);
        this.getStats().put(Stats.TRUE_DEFENCE, 10f);
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
