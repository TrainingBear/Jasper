package me.jasper.jasperproject.JMinecraft.Item.Product.Series.Titanium

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Titanium_Leggings :
    JItem("Titanium Leggings", Material.IRON_LEGGINGS, Rarity.UNCOMMON, ItemType.HELMET, "TITANIUM_LEGGINGS"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.DEFENCE] = 9f
        stats[Stats.HEALTH] = 8f
        stats[Stats.MINING_FORTUNE] = 4f
        stats[Stats.DURABILITY] = 580f // melebihi diamond
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}
