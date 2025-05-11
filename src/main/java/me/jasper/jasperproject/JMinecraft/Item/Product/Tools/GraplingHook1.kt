package me.jasper.jasperproject.JMinecraft.Item.Product.Tools

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Grappling_Hook
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchants.Sharpness
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import net.kyori.adventure.text.Component
import org.bukkit.Material

class GraplingHook : JItem("Grappling Hook", Material.FISHING_ROD, Rarity.COMMON, ItemType.ROD, "GRAPPLING_HOOK"),
    Factory {
    init {
        this.setUpgradeable(false)
        abilities.add(Grappling_Hook(1.5f))
        enchants.add(Sharpness())
    }

    override fun create(): JItem {
        return this
    }

    override fun createLore(): List<Component> {
        return listOf()
    }
}
