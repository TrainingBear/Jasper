package me.jasper.jasperproject.JasperItem.Product.Tools;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class Blender extends Jitem implements Factory {
    public Blender(){
        super("Blender", Material.DIAMOND_HORSE_ARMOR, Rarity.MYTHIC, ItemType.ITEM, 1132L, "ANIMATE");
        this.getAbilities().add(new Animator());
        this.getCustom_lore().addAll(
                List.of(
                        MiniMessage.miniMessage().deserialize("<dark_red><b>THIS IS TEST ITEM")
                        ,MiniMessage.miniMessage().deserialize("<dark_red><b>MAY BE DELETED IN THE FUTURE")
                        ,MiniMessage.miniMessage().deserialize("")
                )
        );
    }
    @Override
    public Jitem create() {
        return this;
    }
}
