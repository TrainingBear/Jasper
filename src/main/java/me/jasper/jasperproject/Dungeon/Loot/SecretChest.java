package me.jasper.jasperproject.Dungeon.Loot;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Block.LootableChest;
import me.jasper.jasperproject.JMinecraft.Loot.Loot;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Map;

public class SecretChest extends LootableChest {
    private final Loot loot = new Loot();
    public SecretChest(Map<String, Long> loot_pair){
        loot.setLoot(loot_pair);
    }

    @Override
    protected Loot getLoot() {
        return loot;
    }
    @Override
    protected void onLoot(InventoryOpenEvent e) {
        e.getPlayer().sendMessage("You looted this chest");
    }
}
