package me.jasper.jasperproject.JasperItem;

import me.jasper.jasperproject.JasperItem.Abilities.Grappling_Hook;
import me.jasper.jasperproject.JasperItem.Abilities.Teleport;
import org.bukkit.Material;

public final class Items {
    public static final Jitem EndGateway;
    static {
        EndGateway = new Jitem("End Gateway V2", Material.DIAMOND_SHOVEL, Rarity.EPIC, ItemType.SWORD, 1L, "Gateway");
        EndGateway.getStats()
                .setDamage(39)
                .setMana(50)
                .setSpeed(10)
                .setAttackSpeed(5);
        EndGateway.getEnchants().add(ENCHANT.SharpnesV2.addLevel());
        EndGateway.getAbilities().add(new Teleport(10, 1));
        try {
            EndGateway.update();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static final Jitem grapling;
    static {
        grapling = new Jitem("Grappling Hook", Material.FISHING_ROD, Rarity.COMMON, ItemType.ROD, 1L, "GRAPLE");
        grapling.setUpgradeable(false);
        grapling.getAbilities().add(new Grappling_Hook(1));
        grapling.getEnchants().add(ENCHANT.SharpnesV2);
        grapling.getLore().add("line1");
        try {
            grapling.update();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
