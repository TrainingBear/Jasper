package me.jasper.jasperproject.JasperItem;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.*;
import me.jasper.jasperproject.JasperItem.Util.ItemManager;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class JItem implements Cloneable{
    @Getter@Setter private long Version; // <---------- 1
    @Setter@Getter private String ID; // <---------- 2
    @Setter private String item_name; // <---------- 3
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
    @Getter private List<Enchant> enchants;
    @Getter private List<ItemAbility> abilities;

    private List<Component> lore = new ArrayList<>();

    /**
     * @param name Items Name Display
     * @param material Items Material
     * @param rarity use Rarity.enum for item rarity
     * @param type use ItemType.enum for item Type category
     * @param ID THIS IS FINAL ID, DONT CHANGE THIS ID! OR ITEM CANT BE UPDATED
     * */

    public JItem(String name, Material material, Rarity rarity, ItemType type, String ID){
        this(false, true, false,
                (byte) 0, name, name, material, rarity, rarity,
                type, 0L, ID, new ArrayList<>(),
                new HashMap<>(), new ArrayList<>());
    }
    public JItem(boolean upgraded, boolean upgrade, boolean UPGRADE,
                 byte occur, String name, String defaultName, Material material,
                 @NotNull Rarity rarity, Rarity baseRarity, @NotNull ItemType type,
                 long itemVersion, String ID, List<ItemAbility> abilities,
                 Map<Stats, Float> stats, List<Enchant> enchant){
        this.item = new ItemStack(material);
        this.abilities = abilities;
        this.stats = stats;
        this.enchants = enchant;
        this.defaultItem_name = defaultName;
        this.item_name = name;
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
            meta.getPersistentDataContainer().set(JKey.CustomName, PersistentDataType.STRING, this.item_name);
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

    protected abstract List<Component> createLore();
    public List<Component> getLore(){
        return createLore();
    }

    public void update() {
        this.lore.clear();
        item.editMeta(meta->{
            meta.getPersistentDataContainer()
                    .set(
                            JKey.Stats,
                            PersistentDataType.TAG_CONTAINER,
                            Stats.toPDC(meta.getPersistentDataContainer().getAdapterContext(), this.stats)
                    );
        });
        lore.addAll(Stats.toLore(this.stats));

        ///         APLLYING ENCHANTS
        if(!enchants.isEmpty()) {
            lore.add(MiniMessage.miniMessage().deserialize("<reset>"));
            item.editMeta(meta->{
                meta.getPersistentDataContainer()
                        .set(
                                JKey.ENCHANT,
                                PersistentDataType.TAG_CONTAINER,
                                Enchant.toPDC(meta.getPersistentDataContainer().getAdapterContext(), enchants, lore)
                        );
            });
        }
        ///         APLLYING ABILITIES
        if(!abilities.isEmpty()){
            item.editMeta(meta->{
                meta.getPersistentDataContainer()
                        .set(
                                JKey.Ability,
                                PersistentDataType.TAG_CONTAINER,
                                ItemAbility.toPDC(meta.getPersistentDataContainer().getAdapterContext(), abilities)
                        );
            });
            lore.addAll(ItemAbility.toLore(abilities));
        }

        buildLore();
        item.editMeta(e->e.displayName(Util.deserialize("<!i>"+this.item_name).color(this.rarity.color)));
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

    private void buildLore(){
        lore.add(MiniMessage.miniMessage().deserialize("<reset>"));
        lore.addAll(createLore());
        lore.addAll(rarity.getDescription(upgraded, type));
        item.editMeta(meta-> meta.lore(lore));
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

    public static JItem convertFrom(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        String name = Util.escapeRegex(PlainTextComponentSerializer.plainText().serialize(meta.displayName()));
        Bukkit.broadcast(Util.deserialize(name).append(meta.displayName()));
        String defaultName = data.get(JKey.CustomName, PersistentDataType.STRING);
        Material material = item.getType();
        Rarity rarity = Rarity.getFromString(Objects.requireNonNull(data.get(JKey.Rarity, PersistentDataType.STRING)));
        Rarity baseRarity = Rarity.getFromString(Objects.requireNonNull(data.get(JKey.BaseRarity, PersistentDataType.STRING)));
        ItemType category = ItemType.getFromString(data.get(JKey.Category, PersistentDataType.STRING));
        long version = data.get(JKey.Version, PersistentDataType.LONG);
        String ID = data.get(JKey.Main, PersistentDataType.STRING);
        byte updatedOCCUR = data.get(JKey.RarityUpdated, PersistentDataType.BYTE);
        boolean upgradeable = Boolean.TRUE.equals(data.get(JKey.UpgradeAble, PersistentDataType.BOOLEAN));
        boolean unlimitedUpgradeable = Boolean.TRUE.equals(data.get(JKey.UnlimitedUpgradeAble, PersistentDataType.BOOLEAN));
        boolean upgraded = Boolean.TRUE.equals(data.get(JKey.Upgraded, PersistentDataType.BOOLEAN));

        Map<Stats, Float> stats = Stats.fromItem(item);
        List<ItemAbility> ability = ItemAbility.convertFrom(item);
        List<Enchant> enchants = Enchant.convertFrom(item);

        JItem convertedItem = new JItem(upgraded, upgradeable, unlimitedUpgradeable, updatedOCCUR,
                name, defaultName, material, rarity, baseRarity, category, version, ID, ability,
                stats, enchants) {
            @Override
            protected List<Component> createLore() {
                return ItemManager.getInstance().getItems().get(ID.toUpperCase()).getLore();
            }
        };
        convertedItem.update();
        return convertedItem;
    }

    public void setMaxStack(int maxStack){
        item.editMeta(e->e.setMaxStackSize(maxStack));
    }
    
    public JItem patch(JItem newVer){
        this.stats = newVer.stats;
        this.abilities = newVer.abilities;
        this.baseRarity = newVer.baseRarity;
        this.defaultItem_name = newVer.defaultItem_name;
        return this;
    }

}
