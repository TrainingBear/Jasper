package me.jasper.jasperproject.JMinecraft.Player.Ability;

import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import net.kyori.adventure.text.Component;

import java.util.List;

public abstract class PlayerAbility extends ItemAbility {
    @Override
    protected List<Component> createLore() {
        return List.of();
    }
}
