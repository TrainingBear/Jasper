package me.jasper.jasperproject.JMinecraft.Item.Product.Series.Titanium

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Titanium_Chestplate :
    JItem("Titanium Chestplate", Material.IRON_CHESTPLATE, Rarity.UNCOMMON, ItemType.CHESTPLATE, "TITANIUM_CHESTPLATE"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.DEFENCE] = 20f
        stats[Stats.HEALTH] = 10f
        stats[Stats.MINING_FORTUNE] = 5f
        stats[Stats.DURABILITY] = 600f // melebihi netherite
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}
