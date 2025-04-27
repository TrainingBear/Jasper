package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity;
import me.jasper.jasperproject.JasperItem.ItemAttributes.*;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Teleport;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchants.Sharpness;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory;
import me.jasper.jasperproject.JMinecraft.Player.Stats;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class TestItem extends JItem implements Factory {
    private static TestItem instance;
    public static TestItem getInstance(){
        if(instance==null){
            instance=new TestItem();
        }
        return instance;
    }
    public TestItem(){
        super("Test Items", Material.NETHERITE_AXE, Rarity.MYTHIC, ItemType.SWORD,  "TEST");
        Map<Stats, Float> stats = this.getStats();
        Random random = new Random();
        for (Stats value : Stats.values()) {
            stats.put(value, random.nextFloat(Float.MAX_VALUE));
        }

        this.getAbilities().add(new Teleport((short) 12, 0));
        this.getEnchants().add(new Sharpness());
        this.setUpgradeable(true);

    }

    @Override
    public JItem create() {
        return this;
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                MiniMessage.miniMessage().deserialize("")
                ,MiniMessage.miniMessage().deserialize("This is the first item line")
                ,MiniMessage.miniMessage().deserialize("This is the Second item line")
                ,MiniMessage.miniMessage().deserialize("so on")
                ,MiniMessage.miniMessage().deserialize("")

        );
    }
}
