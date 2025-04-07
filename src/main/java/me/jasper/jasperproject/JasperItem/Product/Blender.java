package me.jasper.jasperproject.JasperItem.Product;

import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemType;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperItem.Util.Factory;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class Blender extends Jitem implements Factory {
    public Blender(){
        super("Blender", Material.DIAMOND_HORSE_ARMOR, Rarity.MYTHIC, ItemType.ITEM, 1132L, "ANIMATE");
        this.getAbilities().add(new Animator());
        this.getCustom_lore().addAll(
                List.of(
                        ChatColor.translateAlternateColorCodes('&',"&4&lWARNING!! THIS IS TEST ITEM"),
                        ChatColor.translateAlternateColorCodes('&',"&4&lMAY BE DELETED IN THE FUTURE"),
                        ""
                )
        );
    }
    @Override
    public Jitem create() {
        return this;
    }
}
