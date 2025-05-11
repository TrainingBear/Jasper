package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons

import lombok.Getter
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Teleport
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchants.Sharpness
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import org.bukkit.Material

class EndGateway : JItem("End Gateway", Material.GOLDEN_SHOVEL, Rarity.EPIC, ItemType.SWORD, "END_GATEWAY"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.DAMAGE] = 39f
        stats[Stats.STRENGTH] = 30f
        stats[Stats.MANA] = 50f
        stats[Stats.SPEED] = 10f
        stats[Stats.ATTACK_SPEED] = 5f
        this.version = version
        enchants.addAll(listOf(Sharpness()))
        abilities.add(Teleport(10, 0.2f))
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }

    companion object {
        @Getter
        private val instance = EndGateway()
    }
}
