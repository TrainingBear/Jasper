package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Item.Util.ItemManager;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.ComponentBuilder;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Enchant extends Event implements Listener, Cloneable {
    private static final HandlerList HANDLER_LIST = new HandlerList();public static HandlerList getHandlerList() {return HANDLER_LIST;}@Override public @NotNull HandlerList getHandlers() {return HANDLER_LIST;}

    private final String name = this.getClass().getSimpleName();
    protected Component display;
    @Getter protected final NamespacedKey key = new NamespacedKey(JasperProject.getPlugin(), this.getClass().getSimpleName());

    @Getter protected int customValue = 0;
    @Getter @Setter protected byte max_level = 5;
    @Getter protected float baseModifier;
    @Getter protected float prestigedModifier = baseModifier;
    @Getter protected float modifier = prestigedModifier;
    @Getter @Setter protected byte maxPrestige = 3;
    @Getter protected byte level = 1;
    @Getter protected byte prestigeLevel = 0;

    public Component getDisplay(){
        if(prestigeLevel>0){
            display = Util.deserialize("<!i>"+"T"+prestigeLevel+"-"+name+" I").color(getPrestigeColor(prestigeLevel));
            return display;
        }
        display = Util.deserialize("<!i>"+name+" "+Util.toRoman(level)).color(NamedTextColor.WHITE);
        return display;
    }

    public abstract List<Component> getLore();

    public ItemStack getBook(){
        return null;
    }

    public byte addLevel(){
        if(this.level==this.max_level){
            Bukkit.broadcastMessage("prestiged!");
            return prestige();
        }
        this.level++;
        this.modifier = prestigedModifier * level;
        updateDisplay();
        return this.level;
    }

    public byte prestige(){
        this.level = 1;
        prestigeLevel++;
        this.prestigedModifier = baseModifier*1.15f*(prestigeLevel);
        this.modifier = prestigedModifier;
        return this.level;
    }

    private void updateDisplay(){
        if(prestigeLevel>0){
            display = Util.deserialize("T"+prestigeLevel+"-"+name +" "+Util.toRoman(this.level)).color(getPrestigeColor(prestigeLevel));
            return;
        }
        this.display = Util.deserialize(name +" "+Util.toRoman(this.level));
    }

    private TextColor getPrestigeColor(byte lvl){
        return switch (lvl){
            case 0 -> NamedTextColor.WHITE;
            case 1 -> NamedTextColor.GREEN;
            case 2 -> NamedTextColor.YELLOW;
            case 3 -> NamedTextColor.GOLD;
            case 4 -> NamedTextColor.RED;
            case 5 -> NamedTextColor.DARK_RED;
            case 6 -> NamedTextColor.LIGHT_PURPLE;
            case 7 -> NamedTextColor.DARK_PURPLE;
            default -> NamedTextColor.DARK_AQUA;
        };
    }

    public static @NotNull PersistentDataContainer toPDC(PersistentDataAdapterContext data, List<Enchant> enchants, @Nullable List<Component> lore){
        PersistentDataContainer pdc = data.newPersistentDataContainer();
        if(enchants.size() <= 5){
            for (Enchant enchant : enchants){
                if(lore!=null){
                    lore.add(enchant.getDisplay());
                    lore.addAll(enchant.getLore());
                }
                PersistentDataContainer enchant_name = pdc.getAdapterContext().newPersistentDataContainer();
                enchant_name.set(JKey.ENCHANT_LEVEL, PersistentDataType.BYTE, enchant.getLevel());
                enchant_name.set(JKey.ENCHANT_MAX_LEVEL, PersistentDataType.BYTE, enchant.getMax_level());
                enchant_name.set(JKey.ENCHANT_MODIFIER, PersistentDataType.FLOAT, enchant.getModifier());
                enchant_name.set(JKey.ENCHANT_BASEMODIFIER, PersistentDataType.FLOAT, enchant.getBaseModifier());
                enchant_name.set(JKey.ENCHANT_PRESTIGEDMODIFIER, PersistentDataType.FLOAT, enchant.getPrestigedModifier());
                enchant_name.set(JKey.ENCHANT_PRESTIGELEVEL, PersistentDataType.BYTE, enchant.getPrestigeLevel());
                enchant_name.set(JKey.ENCHANT_CUSTOMVALUE, PersistentDataType.INTEGER, enchant.getCustomValue());
                enchant_name.set(JKey.ENCHANT_MAXPRESTIGE, PersistentDataType.BYTE, enchant.getMaxPrestige());
                pdc.set(enchant.getKey(), PersistentDataType.TAG_CONTAINER, enchant_name);
            }
        }
        else {
            byte operator = 0;
            ComponentBuilder builder = new ComponentBuilder();
            for (Enchant enchant : enchants) {
                if(operator%3==0){
                    if(lore!=null) lore.add(builder.getComponent());
                    builder = new ComponentBuilder();
                }
                builder.append(enchant.getDisplay()).append(", ");
                operator++;
                PersistentDataContainer enchant_name = pdc.getAdapterContext().newPersistentDataContainer();
                enchant_name.set(JKey.ENCHANT_LEVEL, PersistentDataType.BYTE, enchant.getLevel());
                enchant_name.set(JKey.ENCHANT_MAX_LEVEL, PersistentDataType.BYTE, enchant.getMax_level());
                enchant_name.set(JKey.ENCHANT_MODIFIER, PersistentDataType.FLOAT, enchant.getModifier());
                enchant_name.set(JKey.ENCHANT_BASEMODIFIER, PersistentDataType.FLOAT, enchant.getBaseModifier());
                enchant_name.set(JKey.ENCHANT_PRESTIGEDMODIFIER, PersistentDataType.FLOAT, enchant.getPrestigedModifier());
                enchant_name.set(JKey.ENCHANT_PRESTIGELEVEL, PersistentDataType.BYTE, enchant.getPrestigeLevel());
                enchant_name.set(JKey.ENCHANT_CUSTOMVALUE, PersistentDataType.INTEGER, enchant.getCustomValue());
                enchant_name.set(JKey.ENCHANT_MAXPRESTIGE, PersistentDataType.BYTE, enchant.getMaxPrestige());
                pdc.set(enchant.getKey(), PersistentDataType.TAG_CONTAINER, enchant_name);
            }
            if(lore!=null) lore.add(builder.getComponent());
        }
        return pdc;
    }

    public static List<Enchant> convertFrom(ItemStack item){
        List<Enchant> enchants = new ArrayList<>();
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if(!pdc.has(JKey.ENCHANT)) return enchants;
        pdc = pdc.get(JKey.ENCHANT, PersistentDataType.TAG_CONTAINER);
        for (Enchant enchants_ : ItemManager.getEnchants()) {
            if (pdc.has(enchants_.getKey())){
                PersistentDataContainer container = pdc.get(enchants_.getKey(), PersistentDataType.TAG_CONTAINER);
                Enchant enchant = (Enchant) enchants_.clone();

                enchant.level = container.get(JKey.ENCHANT_LEVEL, PersistentDataType.BYTE);
                enchant.max_level = container.get(JKey.ENCHANT_MAX_LEVEL, PersistentDataType.BYTE);
                enchant.modifier = container.get(JKey.ENCHANT_MODIFIER, PersistentDataType.FLOAT);
                enchant.baseModifier = container.get(JKey.ENCHANT_BASEMODIFIER, PersistentDataType.FLOAT);
                enchant.prestigedModifier = container.get(JKey.ENCHANT_PRESTIGEDMODIFIER, PersistentDataType.FLOAT);
                enchant.prestigeLevel = container.get(JKey.ENCHANT_PRESTIGELEVEL, PersistentDataType.BYTE);
                enchant.customValue = container.get(JKey.ENCHANT_CUSTOMVALUE, PersistentDataType.INTEGER);
                enchant.maxPrestige = container.get(JKey.ENCHANT_MAXPRESTIGE, PersistentDataType.BYTE);

                enchants.add(enchant);
            }
        }
        return enchants;
    }

    @Override
    public Object clone() {
        try{
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

