package me.jasper.jasperproject.JasperItem;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.*;
import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JItem implements Cloneable{
    @Getter@Setter private long Version; // <---------- 1
    @Setter@Getter private String ID; // <---------- 2
    @Setter private Component item_name; // <---------- 3
    @Getter @Setter private String defaultItem_name; // <---------- 4
    @Setter private boolean upgradeable = true; // <---------- 5
    @Setter private boolean unlimitedUpgradeable = false; // <---------- 6
    private boolean upgraded = false; // <---------- 7
    @Getter private byte upgradesOccur; // <---------- 8
    @Getter private ItemStack item;
    @Getter @Setter private Rarity baseRarity; // <---------- 9
    @Getter @Setter private Rarity rarity; // <---------- 10
    final private ItemType type; // <---------- 11

    @Getter private Map<Stats, Float> stats;
    @Getter private List<ENCHANT> enchants;
    @Getter private List<ItemAbility> abilities;

    private List<Component> lore = new ArrayList<>();
    @Getter private List<Component> custom_lore = new ArrayList<>();

    /**
     * @param name Items Name Display
     * @param material Items Material
     * @param rarity use Rarity.enum for item rarity
     * @param type use ItemType.enum for item Type category
     * @param ID THIS IS FINAL ID, DONT CHANGE THIS ID! OR ITEM CANT BE UPDATED
     * */

    public JItem(String name, Material material, Rarity rarity, ItemType type, String ID){
        this(false, true, false, (byte) 0, name, name, material, rarity, rarity, type, 0L, ID, new ArrayList<>(), new HashMap<>(), new ArrayList<>());
    }
    public JItem(String name, Material material, Rarity rarity,
                 ItemType type, long itemVersion, String ID){
        this(false, true, false, (byte) 0,
                name, name, material, rarity, rarity, type, itemVersion,
                ID, new ArrayList<>(), new HashMap<>(), new ArrayList<>());
    }

    public JItem(boolean upgraded, boolean upgrade, boolean UPGRADE,
                 byte occur, String name, String defaultName, Material material,
                 @NotNull Rarity rarity, Rarity baseRarity, @NotNull ItemType type,
                 long itemVersion, String ID, List<ItemAbility> abilities,
                 Map<Stats, Float> stats, List<ENCHANT> enchant){
        this.item = new ItemStack(material);
        this.abilities = abilities;
        this.stats = stats;
        this.enchants = enchant;
        this.item_name = MiniMessage.miniMessage().deserialize("<!i>"+MiniMessage.miniMessage().serialize(rarity.color)+name);
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
        item.editMeta(meta->{
            meta.getPersistentDataContainer().set(JKey.Main, PersistentDataType.STRING, ID);
            meta.getPersistentDataContainer().set(JKey.CustomName, PersistentDataType.STRING, PlainTextComponentSerializer.plainText().serialize(this.item_name));
            meta.getPersistentDataContainer().set(JKey.Version, PersistentDataType.LONG, itemVersion);
            meta.getPersistentDataContainer().set(JKey.Rarity, PersistentDataType.STRING, rarity.name());
            meta.getPersistentDataContainer().set(JKey.BaseRarity, PersistentDataType.STRING, rarity.name());
            meta.getPersistentDataContainer().set(JKey.Category, PersistentDataType.STRING, type.name());
            meta.getPersistentDataContainer().set(JKey.RarityUpdated, PersistentDataType.BYTE, upgradesOccur);
            meta.getPersistentDataContainer().set(JKey.UpgradeAble, PersistentDataType.BOOLEAN, upgradeable);
            meta.getPersistentDataContainer().set(JKey.UnlimitedUpgradeAble, PersistentDataType.BOOLEAN, unlimitedUpgradeable);
            meta.getPersistentDataContainer().set(JKey.Upgraded, PersistentDataType.BOOLEAN, upgraded);
        });
    }

    public void update() {
        this.lore.clear();
        applyItemStats();
        applyItemEnchantments();
        applyItemAbilities();
        buildLore();
    }

    public void send(Player player){
        player.getInventory().addItem(item.clone());
    }

    public boolean upgrade(){
        if(unlimitedUpgradeable || (!upgradeable || upgraded || rarity == Rarity.MYTHIC)) return false;
        this.upgraded = true;
        this.rarity = rarity.upgrade();
        item.editMeta(meta->meta.getPersistentDataContainer().set(JKey.Rarity, PersistentDataType.STRING, rarity.name()));
        upgradesOccur++;
        return true;
    }

    public void updateRarity(){
        this.rarity = rarity.update(baseRarity, upgradesOccur);
        item.editMeta(meta->meta.getPersistentDataContainer().set(JKey.Rarity, PersistentDataType.STRING, rarity.name()));

    }

    public void replaceStats(Map<Stats, Float> stats){
        this.stats = stats;
    }

    private void applyItemStats() {
        item.editMeta(meta->{
            meta.getPersistentDataContainer()
                    .set(
                            JKey.Stats,
                            PersistentDataType.TAG_CONTAINER,
                            Stats.toPDC(meta.getPersistentDataContainer().getAdapterContext(), this.stats)
                    );
        });
        lore.addAll(Stats.toLore(this.stats));
    }

    private void applyItemEnchantments(){
        if(enchants.isEmpty()) return;
        item.editMeta(meta->{
            meta.getPersistentDataContainer()
                    .set(
                            JKey.Enchant,
                            PersistentDataType.TAG_CONTAINER,
                            getEnchantsdata(meta.getPersistentDataContainer())
                    );
        });
    }

    public void applyItemAbilities(){
        if(abilities.isEmpty()) return;
        item.editMeta(meta->{
            meta.getPersistentDataContainer()
                    .set(
                            JKey.Ability,
                            PersistentDataType.TAG_CONTAINER,
                            ItemAbility.toPDC(meta.getPersistentDataContainer().getAdapterContext(), abilities, lore)
                    );
        });
    }

    private void buildLore(){
        lore.add(MiniMessage.miniMessage().deserialize("<reset>"));
        lore.addAll(custom_lore);
        lore.addAll(rarity.getDescription(upgraded, type));
        item.editMeta(meta-> meta.lore(lore));
    }

    private PersistentDataContainer getEnchantsdata(PersistentDataContainer data){
        lore.add(MiniMessage.miniMessage().deserialize("<reset>"));
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
            else {
                byte operator = 0;
                StringBuilder builder = new StringBuilder();
                for (ENCHANT enchant : enchants) {
                    if(operator%3==0){
                        lore.add(MiniMessage.miniMessage().deserialize("<!i>"+ builder));
                        builder = new StringBuilder();
                    }
                    builder.append(MiniMessage.miniMessage().serialize(enchant.getDisplay()));
                    operator++;

                    enchants_name.set(
                            enchant.getKey(),
                            PersistentDataType.BYTE, enchant.getLevel()
                    );
                }
                lore.add(MiniMessage.miniMessage().deserialize("<!i>"+ builder));
            }
        return enchants_name;
    }

    @Override
    public JItem clone() {
        try{
            JItem clone = (JItem) super.clone();
            clone.abilities = List.copyOf(this.abilities);
            clone.stats = Map.copyOf(this.stats);
            clone.enchants = List.copyOf(this.enchants);
            clone.item = this.item.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static JItem convertFrom(ItemStack item, List<Component> custom_lore) throws IllegalAccessException {
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

        Map<Stats, Float> stats = Stats.fromItem(item);
        List<ItemAbility> ability = ItemAbility.convertFrom(item);
        List<ENCHANT> enchants = ENCHANT.convertFrom(meta);

        JItem convertedItem = new JItem(upgraded, upgradeable, unlimitedUpgradeable, updatedOCCUR,
                name, defaultName, material, rarity, baseRarity, category, version, ID, ability,
                stats, enchants);
        convertedItem.getCustom_lore().clear();
        convertedItem.getCustom_lore().addAll(custom_lore);
        convertedItem.update();
        return convertedItem;
    }

    public void setMaxStack(int maxStack){
        item.editMeta(e->e.setMaxStackSize(maxStack));
    }
    
    public JItem patch(JItem newVer){
        this.stats = newVer.stats;
        this.custom_lore = newVer.getCustom_lore();
        this.abilities = newVer.abilities;
        this.baseRarity = newVer.baseRarity;
        this.defaultItem_name = newVer.defaultItem_name;
        return this;
    }

}
