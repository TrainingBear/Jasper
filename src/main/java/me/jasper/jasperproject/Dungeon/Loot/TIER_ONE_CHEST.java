package me.jasper.jasperproject.Dungeon.Loot;

import me.jasper.jasperproject.JMinecraft.Item.Product.Tools.Blender;
import me.jasper.jasperproject.JMinecraft.Item.Product.Weapons.End_Gateway;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Map;

public class TIER_ONE_CHEST extends SecretChest{
    public static final TIER_ONE_CHEST INSTANCE;
    static {
        Map<String, Long> id = Map.of(End_Gateway.getInstance().getID(), 50L,
                new Blender().getID(), 50L
        );
        INSTANCE = new TIER_ONE_CHEST(id);
    }
    public TIER_ONE_CHEST(Map<String, Long> loot_pair) {
        super(loot_pair);
    }

    @Override
    protected void onLoot(InventoryOpenEvent e) {
        e.getPlayer().sendMessage("You looted Tier 1 Chest!");
        super.onLoot(e);
    }
}
