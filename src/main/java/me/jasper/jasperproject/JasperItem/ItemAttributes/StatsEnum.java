package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum StatsEnum {
    DAMAGE("⚔","Damage",MiniMessage.miniMessage().deserialize("<red>")),
    Damage(true),

    STRENGTH("❁","Strength",MiniMessage.miniMessage().deserialize("<color:#ff1e00>")),
    Strength(true),

    CRIT_DAMAGE("✴","Crit damage",MiniMessage.miniMessage().deserialize("<color:#6245ff>")),
    Crit_damage(true),

    CRIT_CHANCE("✧","Crit Chance",MiniMessage.miniMessage().deserialize("<color:#8B76ff>")),
    Crit_chance(true),

    MANA("✎","Mana",MiniMessage.miniMessage().deserialize("<color:#3f9fff>")),
    Mana(true),

    SPEED("➠","Speed",MiniMessage.miniMessage().deserialize("<color:#ff4fd0>")),
    Speed(true),

    ATTACK_SPEED("⥂","Attack speed",MiniMessage.miniMessage().deserialize("<yellow>")),
    Attack_speed(true),

    DOUBLE_ATTACK("⫻","Double attack",MiniMessage.miniMessage().deserialize("<color:#ffB94C>")),
    Double_attack(true),

    SWING_RANGE("⌀","Swing range",MiniMessage.miniMessage().deserialize("<color:#ff8a63>")),
    Swing_range(true),

    DEFENCE("🛡","Defense",MiniMessage.miniMessage().deserialize("<color:#00ff3c>")),
    Defence(true),

    TRUE_DEFENCE("⛨","True defense",MiniMessage.miniMessage().deserialize("<color:#b5ff7f>")),
    True_defence(true),

    HEALTH("❤","Health" , MiniMessage.miniMessage().deserialize("<red>")),
    Health(true);

    @Getter String symbol;
    @Getter String name;
    @Getter Component color;
    boolean modifier = false;
    StatsEnum(String symbol, String name, Component color){
        this.symbol = symbol;
        this.name = name;
        this.color = color;
    }
    StatsEnum(boolean b){
        modifier = b;
    }

    public String rawSym(){
        return this.symbol;
    }
    public String rawColor(){
        return MiniMessage.miniMessage().serialize(color);
    }

    public Component symWcolor() {
        return MiniMessage.miniMessage().deserialize(rawColor()+this.symbol);
    }
}
