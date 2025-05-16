package me.jasper.jasperproject.JMinecraft.Item.Product.Series.Titanium

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material

class Titanium_Sword : JItem("Titanium Sword", Material.IRON_SWORD, Rarity.UNCOMMON, ItemType.SWORD, "TITANIUM_SWORD"),
    Factory {
    init {
        val stats = this.stats
        stats[Stats.DAMAGE] = 18f
        stats[Stats.STRENGTH] = 10f
        stats[Stats.CRIT_DAMAGE] = 30f
        stats[Stats.CRIT_CHANCE] = 15f
        stats[Stats.ATTACK_SPEED] = 3f
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return java.util.List.of(
            MiniMessage.miniMessage().deserialize("<!i><dark_gray>This type of blade was once forged"),
            MiniMessage.miniMessage().deserialize("<!i><dark_gray>by the greatest blacksmith of his"),
            MiniMessage.miniMessage().deserialize("<!i><dark_gray>time, but nowadays this blade just"),
            MiniMessage.miniMessage().deserialize("<!i><dark_gray>an ordinary titanium blade"),
            MiniMessage.miniMessage().deserialize("")
        )
    }
}
