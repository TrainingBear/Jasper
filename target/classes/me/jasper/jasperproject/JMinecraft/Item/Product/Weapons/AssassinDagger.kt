package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.BackStab
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import me.jasper.jasperproject.Util.Util
import net.kyori.adventure.text.Component
import org.bukkit.Material

class AssassinDagger :
    JItem("Assassin Dagger", Material.DIAMOND_SWORD, Rarity.LEGENDARY, ItemType.SWORD, "ASSASSIN_DAGGER"),
    Factory {
    init {
        abilities.add(BackStab(30f, 10))
        val stats = this.stats
        stats[Stats.DAMAGE] = 130f
        stats[Stats.STRENGTH] = 100f
        stats[Stats.ATTACK_SPEED] = 130f
        stats[Stats.SPEED] = 130f
        stats[Stats.TRUE_DEFENCE] = 10f
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf(
            Util.deserialize("<!i><dark_gray>This dagger has a faint blood on it"),
            Util.deserialize("<!i><dark_gray>looks like this thing has been passed"),
            Util.deserialize("<!i><dark_gray>many bloodies experiences"),
            Util.deserialize("")
        )
    }
}
