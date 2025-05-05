package me.jasper.jasperproject.JMinecraft.Player.Ability;

import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.JasperEvent;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class PlayerAbility extends JasperEvent {
    protected short level;
}