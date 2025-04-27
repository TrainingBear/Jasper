package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Getter;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public enum Rarity {
    COMMON(NamedTextColor.WHITE, 0f, (byte) 0),
    UNCOMMON(NamedTextColor.GREEN, 2.5f, (byte) 1),
    RARE(NamedTextColor.BLUE, 5.0f, (byte) 2),
    EPIC(NamedTextColor.DARK_BLUE, 7.5f, (byte) 3),
    LEGENDARY(NamedTextColor.GOLD, 10.0f, (byte) 4),
    MYTHIC(NamedTextColor.LIGHT_PURPLE, 12.5f, (byte) 5);

    public final TextColor color;
    @Getter private final byte weight;
    @Getter private final float boosterModifier;
    Rarity(TextColor color, float boosterModifier, byte weight) {
        this.weight = weight;
        this.color = color;
        this.boosterModifier = boosterModifier;
    }

    public List<Component> getDescription(boolean upgraded, ItemType type){
        return upgraded?
                List.of(
                    Util.deserialize("<!i><bold>UPGRADED "+name()+String.format(" %s",type.name())).color(this.color),
                    Util.deserialize("<!i><gray>Boost this item stats by <green>"+boosterModifier + "%"))
                :
                List.of(Util.deserialize("<!i><bold>"+name()+String.format(" %s",type.name())).color(this.color),
                        Util.deserialize("<!i><gray>Boost this item stats by <green>"+boosterModifier + "%"));
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
