package me.jasper.jasperproject.JasperItem;

import org.bukkit.ChatColor;

public enum Rarity {
    COMMON,
    UNCOMMON(ChatColor.GREEN),
    RARE(ChatColor.BLUE),
    EPIC(ChatColor.DARK_PURPLE),
    LEGENDARY(ChatColor.GOLD),
    MYTHIC(ChatColor.GOLD);

    final ChatColor color;
    Rarity(ChatColor color){
        this.color = color;
    }
    Rarity(){
        color = ChatColor.WHITE;
    }
}
