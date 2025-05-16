package me.jasper.jasperproject.JMinecraft.Item.Product.Series.Titanium

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Titanium_Pickaxe :
    JItem("Titanium Pickaxe", Material.IRON_PICKAXE, Rarity.UNCOMMON, ItemType.PICKAXE, "TITANIUM_PICKAXE"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.MINING_SPEED] = 60f
        stats[Stats.MINING_FORTUNE] = 13f
        stats[Stats.DAMAGE] = 10f
        stats[Stats.DURABILITY] = 1930f // melebihi diamond
        stats[Stats.BREAK_POWER] = 2f
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}
