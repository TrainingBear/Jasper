package me.jasper.jasperproject.JMinecraft.Player.Util;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public enum DamageType {
    MELEE("\uD83D\uDDE1", NamedTextColor.RED),
    MAGIC("✧", NamedTextColor.LIGHT_PURPLE),
    PROJECTILE("➶", NamedTextColor.DARK_RED),/// Arrow, Bullet, Wind, etc
    POISON("💀", NamedTextColor.DARK_GREEN),
    FIRE("🔥", NamedTextColor.GOLD),
    ABSTRACT("$", NamedTextColor.WHITE);

    private final String symbol;
    private final NamedTextColor color;

    DamageType(String symbol, NamedTextColor color) {
        this.symbol = symbol;
        this.color = color;
    }
}
