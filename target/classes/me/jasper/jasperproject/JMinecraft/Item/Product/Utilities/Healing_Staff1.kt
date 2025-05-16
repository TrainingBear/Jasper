package me.jasper.jasperproject.JMinecraft.Item.Product.Utilities

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Heal
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Healing_Staff : JItem("Healing Staff", Material.BREEZE_ROD, Rarity.EPIC, ItemType.STAFF, "HEALING_STAFF"),
    Factory {
    init {
        this.setMaxStack(1)
        val stats = this.stats
        stats[Stats.HEALTH] = 75f
        stats[Stats.MANA] = 50f
        stats[Stats.MENDING] = 10f
        stats[Stats.DEFENCE] = 15f
        abilities.add(Heal(40, 2f))
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}
