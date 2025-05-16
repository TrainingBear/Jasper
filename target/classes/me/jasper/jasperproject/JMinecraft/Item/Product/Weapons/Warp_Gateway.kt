package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Teleport
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Warper
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Warp_Gateway : JItem("Warp Gateway", Material.DIAMOND_SHOVEL, Rarity.LEGENDARY, ItemType.SWORD, "WARP_GATEWAY"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.DAMAGE] = 45f
        stats[Stats.STRENGTH] = 36f
        stats[Stats.MANA] = 75f
        stats[Stats.SPEED] = 15f
        stats[Stats.ATTACK_SPEED] = 8f
        abilities.add(Teleport(12, 0.2f))
        abilities.add(Warper(20f, 20))
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}
