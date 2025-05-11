package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Burst_Arrow
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Burst_Bow : JItem("Burst Bow", Material.BOW, Rarity.RARE, ItemType.BOW, "BURST_BOW"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.DAMAGE] = 40f
        stats[Stats.STRENGTH] = 20f
        stats[Stats.ATTACK_SPEED] = 10f
        stats[Stats.CRIT_DAMAGE] = 40f
        stats[Stats.CRIT_CHANCE] = 5f
        abilities.add(Burst_Arrow(25f, 5))
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}
