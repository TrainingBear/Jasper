package me.jasper.jasperproject.JMinecraft.Item.Product.Tools;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Animator;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;

public class Blender extends JItem implements Factory {
    public Blender(){
        super("Blender", Material.DIAMOND_HORSE_ARMOR, Rarity.MYTHIC, ItemType.ITEM, "ANIMATE");
        this.getAbilities().add(new Animator());
    }
    @Override
    public JItem create() {
        return this;
    }

    @Override
    protected List<Component> createLore() {
        return  List.of(
                        MiniMessage.miniMessage().deserialize("<dark_red><b>THIS IS TEST ITEM")
                        ,MiniMessage.miniMessage().deserialize("<dark_red><b>MAY BE DELETED IN THE FUTURE")
                        ,MiniMessage.miniMessage().deserialize("")
                );
    }
}
