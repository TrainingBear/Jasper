package me.jasper.jasperproject.JMinecraft.Player.Ability;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.JasperEvent;

@Getter
public abstract class PlayerAbility extends JasperEvent {
    protected short level;
}