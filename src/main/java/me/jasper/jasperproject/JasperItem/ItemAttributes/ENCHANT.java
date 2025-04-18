package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public enum ENCHANT {
    SharpnesV2(20, "Sharpnes", (byte) 6,
            MiniMessage.miniMessage().deserialize("<!i><gray>Deal <count> more damage")
    );

    @Getter private int prestige_level;
    @Getter private double modifier;
    private final Component name;
    @Getter private Component display;
    @Getter private byte level = 1;
    private final byte max_level;
    @Getter private Component lore;
    @Getter private final NamespacedKey key;

    ENCHANT(int modifier, String name, byte max_level, Component lore) {
        this.modifier = modifier;
        this.name = MiniMessage.miniMessage().deserialize("<!i><white>"+name);
        this.max_level = max_level;
        this.lore = lore;
        this.key = new NamespacedKey(JasperProject.getPlugin(),name);
        updateLore();
    }


    private void updateLore(){
//        for (String s : base_lore) {
//            int index = lore.indexOf(s);
//            lore.remove(s);
//        lore = String.format(lore, modifier*level);
        lore = lore.replaceText(TextReplacementConfig.builder().match("<count>")
                .replacement(Component.text(""+modifier*level)).build());//, Placeholder.unparsed("count",""+modifier*level));
//            lore.add(index,s);
//        }
    }


    public ENCHANT addLevel(){
        if(level > max_level) {
            return prestige();
        }
        this.level++;
        this.display = MiniMessage.miniMessage().deserialize(MiniMessage.miniMessage().serialize(name)+" "+Roman(this.level));
        updateLore();
        return this;
    }

    public void decreaseLevel(){
        if(level <=0 ) return;
        this.level--;
        updateLore();
    }

    public ENCHANT prestige(){
        if(this.prestige_level > 4) return null;
        this.prestige_level++;
        this.modifier *= 1.25;
        this.level = 1;
        this.display = MiniMessage.miniMessage().deserialize(MiniMessage.miniMessage().serialize(name)+" "+Roman(this.level));
        this.display = switch (prestige_level){
            case 1 -> MiniMessage.miniMessage().deserialize("<!i><white>T1"+display);
            case 2 -> MiniMessage.miniMessage().deserialize("<!i><green>T2"+display);
            case 3 -> MiniMessage.miniMessage().deserialize("<!i><blue>T3"+display);
            case 4 -> MiniMessage.miniMessage().deserialize("<!i><dark_purple>T3"+display);
            default -> throw new IllegalStateException("Unexpected value: " + prestige_level);
        };
        updateLore();
        return this;
    }

    public static String Roman(int numbers){
        return numberToRoman(numbers, 0, new Stack<>());
    }

    private static final String[] romanUnique = {"I","V","X","L","C","D","M","V̅","X̅"};
    private static String numberToRoman(int numbers, int digit, Stack<String> memory){

        if(numbers<=0){
            StringBuilder builder = new StringBuilder();
            while (!memory.isEmpty()){
                builder.append(memory.pop());
            }
            return builder.toString();
        }

        int last_digit = numbers % 10;
        StringBuilder roman = new StringBuilder();
        if(last_digit==9){
            roman.append(romanUnique[digit]);
            roman.append(romanUnique[digit+2]);
            memory.add(roman.toString());
            return numberToRoman(numbers/10, digit+2, memory);
        }
        if(last_digit>=5){
            roman.append(romanUnique[digit+1]);
            roman.append(romanUnique[digit].repeat(last_digit - 5));
            memory.add(roman.toString());
            return numberToRoman(numbers/10, digit+2, memory);
        }
        if(last_digit==4){
            roman.append(romanUnique[digit]);
            roman.append(romanUnique[digit+1]);
            memory.add(roman.toString());
            return numberToRoman(numbers/10, digit+2, memory);
        }
        if(last_digit==0){
            return numberToRoman(numbers/10, digit+2, memory);
        }
        roman.append(romanUnique[digit].repeat(last_digit));
        memory.add(roman.toString());
        return numberToRoman(numbers/10, digit+2, memory);
    }

    public static List<ENCHANT> convertFrom(ItemMeta meta){
        List<ENCHANT> enchants = new ArrayList<>();
        if(!ItemUtils.hasEnchants(meta)) return enchants;
        for (ENCHANT enchant : ENCHANT.values()) {
            if(ItemUtils.getEnchants(meta).has(enchant.key)) enchants.add(enchant);
        }
        return enchants;
    }

}

