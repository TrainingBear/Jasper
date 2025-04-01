package me.jasper.jasperproject.JasperItem;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.*;
import me.jasper.jasperproject.JasperItem.Util.ItemHandler;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.JasperItem.Util.JKey;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Jitem {
    @Getter private long Version; // <---------- 1
    @Getter private String ID; // <---------- 2
    @Setter private String item_name; // <---------- 3
    @Getter @Setter private String defaultItem_name; // <---------- 4
    @Setter private boolean upgradeable = true; // <---------- 5
    @Setter private boolean unlimitedUpgradeable = false; // <---------- 6
    private boolean upgraded = false; // <---------- 7
    @Getter private byte upgradesOccur; // <---------- 8
    @Getter private ItemStack item;
    @Getter @Setter private Rarity baseRarity; // <---------- 9
    private Rarity rarity; // <---------- 10
    private ItemMeta meta;
    final private ItemType type; // <---------- 11

    @Getter private ItemStats stats;
    @Getter private List<ENCHANT> enchants;
    // List<GemstoneAttribute> gemstoneSlot = new ArrayList<>();
    @Getter private List<ItemAbility> abilities;

    @Getter private List<String> lore = new ArrayList<>();
    @Getter private List<String> custom_lore = new ArrayList<>();

    /**
     * @param name Item Name Display
     * @param material Item Material
     * @param rarity use Rarity.enum for item rarity
     * @param type use ItemType.enum for item Type category
     * @param itemVersion THIS IS ITEM VERSION, PLEASE CHANGE THE VERSION IF UPDATE ARE NEEDED
     * @param ID THIS IS FINAL ID, DONT CHANGE THIS ID! OR ITEM CANT BE UPDATED
     * */

    public Jitem(String name, Material material, Rarity rarity, ItemType type,
                 long itemVersion, String ID){
        this(false, true, false, (byte) 0, name, name, material, rarity, rarity, type, itemVersion, ID, new ArrayList<>(), new ItemStats(), new ArrayList<>());
    }

    public Jitem(boolean upgraded, boolean upgrade, boolean UPGRADE, byte occur, String name,
                 String defaultName, Material material, Rarity rarity, Rarity baseRarity, ItemType type,
                 long itemVersion, String ID, List<ItemAbility> abilities, ItemStats stats, List<ENCHANT> enchant){
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
        this.abilities = abilities;
        this.stats = stats;
        this.enchants = enchant;

        this.item_name = rarity.color+ name;
        this.defaultItem_name = defaultName;
        this.baseRarity = baseRarity;
        this.rarity = rarity;
        this.upgradeable = upgrade;
        this.unlimitedUpgradeable = UPGRADE;
        this.upgraded = upgraded;
        this.upgradesOccur = occur;
        this.type = type;
        this.ID = ID;
        this.Version = itemVersion;
        meta.getPersistentDataContainer().set(JKey.Main, PersistentDataType.STRING, ID);
        meta.getPersistentDataContainer().set(JKey.CustomName, PersistentDataType.STRING, this.item_name);
        meta.getPersistentDataContainer().set(JKey.Version, PersistentDataType.LONG, itemVersion);
        meta.getPersistentDataContainer().set(JKey.Rarity, PersistentDataType.STRING, rarity.name());
        meta.getPersistentDataContainer().set(JKey.BaseRarity, PersistentDataType.STRING, rarity.name());
        meta.getPersistentDataContainer().set(JKey.Category, PersistentDataType.STRING, type.name());
        meta.getPersistentDataContainer().set(JKey.RarityUpdated, PersistentDataType.BYTE, upgradesOccur);
        meta.getPersistentDataContainer().set(JKey.UpgradeAble, PersistentDataType.BOOLEAN, upgradeable);
        meta.getPersistentDataContainer().set(JKey.UnlimitedUpgradeAble, PersistentDataType.BOOLEAN, unlimitedUpgradeable);
        meta.getPersistentDataContainer().set(JKey.Upgraded, PersistentDataType.BOOLEAN, upgraded);
        this.meta.setItemName(item_name);
    }

    /**
     * This is used by ItemPatcher
     * */
    public void setUpdateable(boolean b){
        if(!b){
            ItemHandler.getItems().remove(ID);
        }
        ItemHandler.getItems().put(ID,this);
    }

    public void update() {
        this.lore.clear();
        try{
            applyItemStats();
        } catch (IllegalAccessException e) {
            Bukkit.getLogger().info("[JasperProject] something went wrong when applying this "+ this.item_name+" item stats");
        }
        applyItemEnchantments();
        applyItemAbilities();
        buildLore();
        item.setItemMeta(meta);
    }

    public void send(Player player){
        player.getInventory().addItem(item);
    }

    public boolean upgrade(){
        if(unlimitedUpgradeable || (!upgradeable || upgraded || rarity == Rarity.MYTHIC)) return false;
        this.upgraded = true;
        this.rarity = rarity.upgrade();
        meta.getPersistentDataContainer().set(JKey.Rarity, PersistentDataType.STRING, rarity.name());
        upgradesOccur++;
        return true;
    }

    public void updateRarity(){
        this.rarity = rarity.update(baseRarity, upgradesOccur);
        meta.getPersistentDataContainer().set(JKey.Rarity, PersistentDataType.STRING, rarity.name());
    }

    public void replaceStats(ItemStats stats){
        this.stats = stats;
    }

    private void applyItemStats() throws IllegalAccessException {
        this.meta.getPersistentDataContainer()
                .set(
                        JKey.Stats,
                        PersistentDataType.TAG_CONTAINER,
                        stats.getAndApplyFinalStats(meta.getPersistentDataContainer())
                );
        this.meta.getPersistentDataContainer()
                .set(
                        JKey.BaseStats,
                        PersistentDataType.TAG_CONTAINER,
                        stats.getBaseStats(meta.getPersistentDataContainer())
                );
        this.meta.getPersistentDataContainer()
                .set(
                        JKey.StatsModifier,
                        PersistentDataType.TAG_CONTAINER,
                        stats.getModifierStats(meta.getPersistentDataContainer())
                );
        item.setItemMeta(this.meta);
        lore.addAll(stats.getLore());
    }

    private void applyItemEnchantments(){
        if(enchants.isEmpty()) return;
        this.meta.getPersistentDataContainer()
                .set(
                        JKey.Enchant,
                        PersistentDataType.TAG_CONTAINER,
                        getEnchantsdata(this.meta.getPersistentDataContainer())
                );
    }


    public void applyItemAbilities(){
        if(abilities.isEmpty()) return;
        this.meta.getPersistentDataContainer()
                .set(
                        JKey.Ability,
                        PersistentDataType.TAG_CONTAINER,
                        getAbilitiesdata(meta.getPersistentDataContainer())
                );
    }

    private void buildLore(){
        lore.add(ChatColor.RESET+"");
        lore.addAll(custom_lore);
        this.lore.addAll(rarity.getDescription(upgraded, type));
        this.meta.setLore(lore);
        this.item.setItemMeta(this.meta);
    }

    private PersistentDataContainer getEnchantsdata(PersistentDataContainer data){
        lore.add(ChatColor.RESET+"");
        PersistentDataContainer enchants_name = data.getAdapterContext().newPersistentDataContainer();
            //this gonna make enchants show the description
            //if the size V
            if(enchants.size() <= 5){
                for (ENCHANT enchant : enchants){
                    lore.add(enchant.getDisplay());
                    lore.add(enchant.getLore());

                    enchants_name.set(
                            enchant.getKey(),
                            PersistentDataType.BYTE, enchant.getLevel()
                    );
                }
            }

            //else yaudah ilang
            else {
                byte operator = 0;
                StringBuilder builder = new StringBuilder();
                for (ENCHANT enchant : enchants) {
                    if(operator%3==0){
                        lore.add(builder.toString());
                        builder = new StringBuilder();
                    }
                    builder.append(enchant.getDisplay());
                    operator++;

                    enchants_name.set(
                            enchant.getKey(),
                            PersistentDataType.BYTE, enchant.getLevel()
                    );
                }
                lore.add(builder.toString());
            }
        return enchants_name;
    }

    private PersistentDataContainer getAbilitiesdata(PersistentDataContainer itemMeta){
        lore.add(ChatColor.RESET+"");
        PersistentDataContainer abilities_name = itemMeta.getAdapterContext().newPersistentDataContainer();
        for (ItemAbility ability : abilities) {
            lore.addAll(ability.getLore());
            abilities_name.set(
                    ability.getKey(),
                    PersistentDataType.TAG_CONTAINER, ability.getStatsContainer(abilities_name)
            );
        }
        return abilities_name;
    }


    public Jitem(Jitem item) throws IllegalAccessException {
        this.item_name = item.item_name;
        this.upgradeable = item.upgradeable;
        this.unlimitedUpgradeable = item.unlimitedUpgradeable;
        this.upgraded = item.upgraded;
        this.item = item.item;
        this.rarity = item.rarity;
        this.meta = this.item.getItemMeta();
        this.type = item.type;
        this.stats = item.stats;
        this.enchants = item.enchants;
        this.abilities = item.abilities;
        this.lore = item.lore;

    }

    @Override
    public Jitem clone() {
        try {
            return new Jitem(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Jitem convertFrom(ItemStack item, List<String> custom_lore) throws IllegalAccessException {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        String name = meta.getItemName();
        String defaultName = data.get(JKey.CustomName, PersistentDataType.STRING);
        Material material = item.getType();
        Rarity rarity = Rarity.getFromString(data.get(JKey.Rarity, PersistentDataType.STRING));
        Rarity baseRarity = Rarity.getFromString(data.get(JKey.BaseRarity, PersistentDataType.STRING));
        ItemType category = ItemType.getFromString(data.get(JKey.Category, PersistentDataType.STRING));
        long version = data.get(JKey.Version, PersistentDataType.LONG);
        String ID = data.get(JKey.Main, PersistentDataType.STRING);
        byte updatedOCCUR = data.get(JKey.RarityUpdated, PersistentDataType.BYTE);
        boolean upgradeable = data.get(JKey.UpgradeAble, PersistentDataType.BOOLEAN);
        boolean unlimitedUpgradeable = data.get(JKey.UnlimitedUpgradeAble, PersistentDataType.BOOLEAN);
        boolean upgraded = data.get(JKey.Upgraded, PersistentDataType.BOOLEAN);

        ItemStats stats = new ItemStats();
        stats.getStatsFromItem(item);

        List<ItemAbility> ability = ItemAbility.convertFrom(item);
        List<ENCHANT> enchants = ENCHANT.convertFrom(meta);

        Jitem convertedItem = new Jitem(upgraded, upgradeable, unlimitedUpgradeable, updatedOCCUR,
                name, defaultName, material, rarity, baseRarity, category, version, ID, ability,
                stats, enchants);
        convertedItem.getCustom_lore().clear();
        convertedItem.getCustom_lore().addAll(custom_lore);
        convertedItem.update();
        return convertedItem;
    }

}
