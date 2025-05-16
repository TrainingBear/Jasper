package me.jasper.jasperproject.JMinecraft.Item.Product.Utilities

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Heal
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Healing_Wand : JItem("Healing Wand", Material.BREEZE_ROD, Rarity.RARE, ItemType.WAND, "HEALING_WAND"),
    Factory {
    init {
        this.setMaxStack(1)
        stats[Stats.HEALTH] = 50f
        stats[Stats.MANA] = 35f
        stats[Stats.DEFENCE] = 10f
        abilities.add(Heal(20, 3f))
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}
