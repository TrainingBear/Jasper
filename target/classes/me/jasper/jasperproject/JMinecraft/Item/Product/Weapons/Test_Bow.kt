package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Test_Bow : JItem("Admin Bow", Material.BOW, Rarity.MYTHIC, ItemType.BOW, "ADMIN-BOW"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.DAMAGE] = 10f
        stats[Stats.STRENGTH] = 600f
        stats[Stats.CRIT_CHANCE] = 50f
        stats[Stats.CRIT_DAMAGE] = 50f
    }

    override fun createLore(): List<Component> {
        return listOf()
    }

    override fun create(): JItem {
        return this
    }
}
