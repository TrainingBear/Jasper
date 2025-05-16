package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons

import lombok.Getter
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Bash
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import me.jasper.jasperproject.Util.Util
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Heavy_Axe : JItem("Heavy Axe", Material.DIAMOND_AXE, Rarity.EPIC, ItemType.AXE, "HEAVY_AXE"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.DAMAGE] = 120f
        stats[Stats.STRENGTH] = 93f
        stats[Stats.CRIT_DAMAGE] = 43f
        stats[Stats.CRIT_CHANCE] = 30f
        abilities.add(Bash(5, 0f))
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return java.util.List.of(
            Util.deserialize("<!i>kapak terberat sedunia, lebih berat dari beban anak <dark_gray>")
        )
    }

    companion object {
        @Getter
        private val instance = Heavy_Axe()
    }
}
