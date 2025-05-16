package me.jasper.jasperproject.JMinecraft.Item.Product.Utilities

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Jumper
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import me.jasper.jasperproject.Util.Util
import net.kyori.adventure.text.Component
import org.bukkit.Material

class FeatherJumper : JItem("Feather Jumper", Material.FEATHER, Rarity.UNCOMMON, ItemType.ITEM, "FEATHER_JUMPER"),
    Factory {
    init {
        abilities.add(Jumper(5, 1f))
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf(
            Util.deserialize("<!i><gray>This feather it looks like has"),
            Util.deserialize("<!i><gray>a some magical physics that"),
            Util.deserialize("<!i><gray>could make you thrown to air")
        )
    }
}
