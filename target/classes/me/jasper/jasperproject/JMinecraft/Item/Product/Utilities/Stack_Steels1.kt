package me.jasper.jasperproject.JMinecraft.Item.Product.Utilities

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.Burnt
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemType
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Rarity
import me.jasper.jasperproject.JMinecraft.Item.JItem
import me.jasper.jasperproject.JMinecraft.Item.Util.Factory
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Stack_Steels : JItem("Stack Steels", Material.FLINT_AND_STEEL, Rarity.UNCOMMON, ItemType.ITEM, "STACK_STEELS"),
    Factory {
    init {
        abilities.add(Burnt(12f))
    }

    override fun createLore(): List<Component> {
        return listOf()
    }

    override fun create(): JItem {
        return this
    }
}
