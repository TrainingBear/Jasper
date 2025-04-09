package me.jasper.jasperproject.JasperItem.ItemAttributes;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.Util.ItemManager;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public enum ENCHANT {
    SharpnesV2(20, "Sharpnes", (byte) 6,
            ChatColor.GRAY+"Deal %s more damage"
    );

    @Getter private int prestige_level;
    @Getter private double modifier;
    private final String name;
    @Getter private String display;
    @Getter private byte level = 1;
    private final byte max_level;
    @Getter private String lore;
    @Getter private final NamespacedKey key;

    ENCHANT(int modifier, String name, byte max_level, String lore) {
        this.modifier = modifier;
        this.name = ChatColor.WHITE+name;
        this.max_level = max_level;
        this.lore = lore;
        this.key = new NamespacedKey(JasperProject.getPlugin(),name);
        updateLore();
    }


    private void updateLore(){
//        for (String s : base_lore) {
//            int index = lore.indexOf(s);
//            lore.remove(s);
        lore = String.format(lore, modifier*level);
//            lore.add(index,s);
//        }
    }


    public ENCHANT addLevel(){
        if(level > max_level) {
            return prestige();
        }
        this.level++;
        this.display = name+" "+Roman(this.level);
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
        this.display = name+" "+Roman(this.level);
        this.display = switch (prestige_level){
            case 1 -> ChatColor.WHITE + "T1";
            case 2 -> ChatColor.GREEN + "T2";
            case 3 -> ChatColor.BLUE + "T3";
            case 4 -> ChatColor.DARK_PURPLE + "T4";
            default -> throw new IllegalStateException("Unexpected value: " + prestige_level);
        }+display;
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

