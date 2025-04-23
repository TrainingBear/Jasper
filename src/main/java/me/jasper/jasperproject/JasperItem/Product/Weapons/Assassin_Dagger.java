package me.jasper.jasperproject.JasperItem.Product.Weapons;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.BackStab;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

public class Assassin_Dagger extends Jitem implements Factory {
    public Assassin_Dagger(){
        super("Assassin Dagger", Material.DIAMOND_SWORD, Rarity.LEGENDARY, ItemType.SWORD,2131L,"ASSASSIN_DAGGER");
        this.getAbilities().add(new BackStab(30f,10));
        this.getCustom_lore().addAll(List.of(
                MiniMessage.miniMessage().deserialize("<!i><gray>This dagger has a faint blood on it")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>looks like this thing has been passed")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>many bloodies experiences")
                ,MiniMessage.miniMessage().deserialize("")
        ));
    }
    @Override
    public Jitem create(){
        return this;
    }
}
