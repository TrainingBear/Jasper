package me.jasper.jasperproject.JMinecraft.Item.Product.Series.Titanium

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Titanium_Shovel :
    JItem("Titanium Shovel", Material.IRON_SHOVEL, Rarity.UNCOMMON, ItemType.SHOVEL, "TITANIUM_SHOVEL"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.MINING_SPEED] = 45f
        stats[Stats.DAMAGE] = 6f
        stats[Stats.DURABILITY] = 1930f // melebihi diamond shovel
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}