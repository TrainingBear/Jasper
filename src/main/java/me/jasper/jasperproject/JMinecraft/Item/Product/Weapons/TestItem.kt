package me.jasper.jasperproject.JMinecraft.Item.Product.Weapons

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Teleport
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchants.Sharpness
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.JMinecraft.Player.Stats
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import java.util.*

class TestItem : JItem("Test Items", Material.NETHERITE_AXE, Rarity.MYTHIC, ItemType.SWORD, "TEST"),
    Factory {
    init {
        val stats = this.stats
        val random = Random()
        for (value in Stats.entries) {
            stats[value] = random.nextFloat(Float.MAX_VALUE)
        }

        abilities.add(Teleport(12.toShort().toInt(), 0f))
        enchants.add(Sharpness())
        this.setUpgradeable(true)
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return java.util.List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("This is the first item line"),
            MiniMessage.miniMessage().deserialize("This is the Second item line"),
            MiniMessage.miniMessage().deserialize("so on"),
            MiniMessage.miniMessage().deserialize("")

        )
    }

    companion object {
        var instance: TestItem? = null
            get() {
                if (field == null) {
                    field = TestItem()
                }
                return field
            }
            private set
    }
}
