package me.jasper.jasperproject.JMinecraft.Item.Product.Series.Titanium

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Titanium_Boots : JItem("Titanium Boots", Material.IRON_BOOTS, Rarity.UNCOMMON, ItemType.HELMET, "TITANIUM_BOOTS"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.DEFENCE] = 18f
        stats[Stats.HEALTH] = 9f
        stats[Stats.MINING_SPEED] = 5f
        stats[Stats.DURABILITY] = 500f // melebihi diamond
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}
