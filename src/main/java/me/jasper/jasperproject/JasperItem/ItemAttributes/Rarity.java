package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.List;

public enum Rarity {
    COMMON(ChatColor.WHITE, 0f, (byte) 0),
    UNCOMMON(ChatColor.GREEN, 2.5f, (byte) 1),
    RARE(ChatColor.BLUE, 5.0f, (byte) 2),
    EPIC(ChatColor.DARK_PURPLE, 7.5f, (byte) 3),
    LEGENDARY(ChatColor.GOLD, 10.0f, (byte) 4),
    MYTHIC(ChatColor.LIGHT_PURPLE, 12.5f, (byte) 5);

    public final ChatColor color;
    @Getter private final byte weight;
    @Getter private final float boosterModifier;
    Rarity(ChatColor color, float boosterModifier, byte weight) {
        this.weight = weight;
        this.color = color;
        this.boosterModifier = boosterModifier;
    }

    public List<String> getDescription(boolean upgraded, ItemType type){

        return upgraded?
                List.of(this.color+""+ChatColor.BOLD+"UPGRADED "+name()+String.format(" %s",type.name()),
                ChatColor.translateAlternateColorCodes('&',"&7Boost this item stats by &a"+ChatColor.GREEN+boosterModifier+"%"))
                :
                List.of(this.color+""+ChatColor.BOLD+this.name()+String.format(" %s",type.name()),
                ChatColor.translateAlternateColorCodes('&',"&7Boost this item stats by &a"+boosterModifier+"%"));
    }

    public Rarity update(Rarity base, byte timesUpdated){
        return getFromWeight((byte) (base.getWeight()+timesUpdated));
    }

    public Rarity upgrade(){
        return switch (this){
            case COMMON -> UNCOMMON;
            case UNCOMMON -> RARE;
            case RARE -> EPIC;
            case EPIC -> LEGENDARY;
            case LEGENDARY, MYTHIC -> MYTHIC;
        };
    }

    public Rarity getFromWeight(byte w){
        return switch (w){
            case 0 -> COMMON;
            case 1 -> UNCOMMON;
            case 2 -> RARE;
            case 3 -> EPIC;
            case 4 -> LEGENDARY;
            default -> MYTHIC;
        };
    }

    public static Rarity getFromString(String r){
        return switch (r){
            case "UNCOMMON" -> UNCOMMON;
            case "RARE" -> RARE;
            case "EPIC" -> EPIC;
            case "LEGENDARY" -> LEGENDARY;
            case "MYTHIC" -> MYTHIC;
            default -> COMMON;
        };
    }
}
