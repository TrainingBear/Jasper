package me.jasper.jasperproject.Bazaar;

import me.jasper.jasperproject.Util.JKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Bazaar {
    static Map<BazaarEnum, BZItemGroup> category = new HashMap<>();
    public static void setCategory(){
        category.put(BazaarEnum.SLIMEFUN_CATEG, new BZItemGroup());

    }


    /// Opening the default Bazaar GUI
    public static void openGUI(Player p){
        p.openInventory(getMainMenu(p));
    }
    /// Opening GUI followed query search
    public static void openGUI(Player p,String query){
        p.openInventory(getSearchMenu(p,query));
    }
    /// GUI when first open the Bazaar
    private static Inventory getMainMenu(Player p){
        Inventory bazaarMenuInventory = Bukkit.createInventory(
                p,54
                , MiniMessage.miniMessage().deserialize(BazaarEnum.TITLE_STRING.get()+" <yellow>Worldwide bazaar"));

        setToSlimefunCateg(bazaarMenuInventory);
        createFrame(bazaarMenuInventory, Material.BROWN_STAINED_GLASS_PANE);

        return bazaarMenuInventory;
    }
    private static Inventory getSearchMenu(Player p,String query){
        Inventory bazaarMenuInventory = Bukkit.createInventory(
                p,54
                , MiniMessage.miniMessage().deserialize(BazaarEnum.TITLE_STRING.get()+" <yellow>Worldwide bazaar"));

        setToSlimefunCateg(bazaarMenuInventory);
        createFrame(bazaarMenuInventory, Material.BROWN_STAINED_GLASS_PANE);

        return bazaarMenuInventory;
    }
    private static void createFrame(Inventory inv,Material m){
        ItemStack categoryPane = new ItemStack(m);
        ItemMeta paneMeta = categoryPane.getItemMeta();
        paneMeta.displayName(MiniMessage.miniMessage().deserialize(""));
        categoryPane.setItemMeta(paneMeta);

        for(byte i = 9;  i<=16; i++ ) inv.setItem(i, ItemBZGUI.makeCusItem(Material.BLACK_STAINED_GLASS_PANE,"",null));
        for(byte i = 45; i<=53; i++ ) inv.setItem(i, categoryPane);
        for(byte i = 9;  i<=27; i+=9) {
            inv.setItem(10+i, categoryPane);
            inv.setItem(17+i, categoryPane);
        }

        inv.setItem(12, ItemBZGUI.getSelectedCategItem(null));
        inv.setItem(49, ItemBZGUI.getTitleItem());
        inv.setItem(18, ItemBZGUI.getSellAllInvItem());
        inv.setItem(27, ItemBZGUI.getSellAllStashItem());
        inv.setItem(36, ItemBZGUI.getNotaItem());

        inv.setItem(8, ItemBZGUI.getCloseItem());
        inv.setItem(7, ItemBZGUI.makeCusItem(Material.RED_STAINED_GLASS_PANE,"",null));
        inv.setItem(17, ItemBZGUI.makeCusItem(Material.RED_STAINED_GLASS_PANE,"",null));
    }

    //NOTE: Index slot 3 is selected

    public static Inventory setToSlimefunCateg(Inventory inv){
        clearCategItem(inv);
        inv.setItem(12, ItemBZGUI.getSelectedCategItem(BazaarEnum.SLIMEFUN_CATEG));
        inv.setItem(0, new ItemStack(Material.SPECTRAL_ARROW));
        inv.setItem(6, new ItemStack(Material.SPECTRAL_ARROW));

        inv.setItem(3, ItemBZGUI.getSlimefunCategItem(true));//selected
        inv.setItem(4, ItemBZGUI.getMobLootCategItem(false));
        inv.setItem(5, ItemBZGUI.getFarmingCategItem(false));


        return inv;
    }
    public static Inventory setToMobLootCateg(Inventory inv){
        clearCategItem(inv);
        inv.setItem(12, ItemBZGUI.getSelectedCategItem(BazaarEnum.MOB_LOOT_CATEG));
        inv.setItem(0, new ItemStack(Material.SPECTRAL_ARROW));
        inv.setItem(6, new ItemStack(Material.SPECTRAL_ARROW));

        inv.setItem(2, ItemBZGUI.getSlimefunCategItem(false));
        inv.setItem(3, ItemBZGUI.getMobLootCategItem(true));//selected
        inv.setItem(4, ItemBZGUI.getFarmingCategItem(false));
        inv.setItem(5, ItemBZGUI.getMiningCategItem(false));


        return inv;
    }
    public static Inventory setToFarmingCateg(Inventory inv){
        clearCategItem(inv);
        inv.setItem(12, ItemBZGUI.getSelectedCategItem(BazaarEnum.FARMING_CATEG));
        inv.setItem(0, new ItemStack(Material.SPECTRAL_ARROW));
        inv.setItem(6, new ItemStack(Material.SPECTRAL_ARROW));

        inv.setItem(1, ItemBZGUI.getSlimefunCategItem(false));
        inv.setItem(2, ItemBZGUI.getMobLootCategItem(false));
        inv.setItem(3, ItemBZGUI.getFarmingCategItem(true));//selected
        inv.setItem(4, ItemBZGUI.getMiningCategItem(false));


        return inv;
    }
    public static Inventory setToMiningCateg(Inventory inv){
        clearCategItem(inv);
        inv.setItem(12, ItemBZGUI.getSelectedCategItem(BazaarEnum.MINING_CATEG));
        inv.setItem(0, new ItemStack(Material.SPECTRAL_ARROW));
        inv.setItem(6, new ItemStack(Material.SPECTRAL_ARROW));

        inv.setItem(1, ItemBZGUI.getMobLootCategItem(false));
        inv.setItem(2, ItemBZGUI.getFarmingCategItem(false));
        inv.setItem(3, ItemBZGUI.getMiningCategItem(true)); //selected


        return inv;
    }



    private static Inventory clearCategItem(Inventory inv){
        for(byte i = 1 ; i<=5; i++) inv.setItem(i, new ItemStack(Material.AIR));
        return inv;
    }



    private static final class ItemBZGUI {




        /// Close GUI ; Barrier
        private static ItemStack getCloseItem(){
            ItemStack item = new ItemStack(Material.BARRIER);
            ItemMeta meta = item.getItemMeta();

            meta.displayName((Component) BazaarEnum.CLOSE_ITEM.get());
            meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("")
                    ,MiniMessage.miniMessage().deserialize("<!i><b><color:#ff261f>Click</color></b> <red>to close")
            ));

            PersistentDataContainer mainTag = meta.getPersistentDataContainer();
            PersistentDataContainer branchMainTag = mainTag.getAdapterContext().newPersistentDataContainer();

            branchMainTag.set(JKey.bazaar_Action, PersistentDataType.STRING, "Close");

            mainTag.set(JKey.bazaar_Item_GUI, PersistentDataType.TAG_CONTAINER, branchMainTag);

            item.setItemMeta(meta);

            return item;
        }
        /// Title GUI ; Hanging sign item thing
        private static ItemStack getTitleItem(){
            ItemStack item = new ItemStack(Material.ACACIA_HANGING_SIGN);
            ItemMeta meta = item.getItemMeta();

            meta.displayName((Component) BazaarEnum.TITLE_NAME_COMPO.get());
            meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("<!i><yellow>Worldwide bazaar")
                    ,MiniMessage.miniMessage().deserialize("")
                    ,MiniMessage.miniMessage().deserialize("<!i>"+BazaarEnum.CLICK_TEXT.get()+" <gray>to search thing")
            ));

            PersistentDataContainer mainTag = meta.getPersistentDataContainer();
            PersistentDataContainer branchMainTag = mainTag.getAdapterContext().newPersistentDataContainer();

            branchMainTag.set(JKey.bazaar_Action, PersistentDataType.STRING, "Search");

            mainTag.set(JKey.bazaar_Item_GUI, PersistentDataType.TAG_CONTAINER, branchMainTag);

            item.setItemMeta(meta);
            return item;
        }
        /// Selected Category ; lime pane
        private static ItemStack getSelectedCategItem(BazaarEnum categ){
            ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize("<!i><green>Selected category"));
            if(categ !=null) switch(categ){
                case SLIMEFUN_CATEG -> meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("")
                    ,MiniMessage.miniMessage().deserialize("<!i><gray>→ "+BazaarEnum.SLIMEFUN_CATEG.get()+" <gray>←")
                ));
                case MOB_LOOT_CATEG -> meta.lore(List.of(
                        MiniMessage.miniMessage().deserialize("")
                        ,MiniMessage.miniMessage().deserialize("<!i><gray>→ "+BazaarEnum.MOB_LOOT_CATEG.get()+" <gray>←")
                 ));
                case FARMING_CATEG -> meta.lore(List.of(
                        MiniMessage.miniMessage().deserialize("")
                        ,MiniMessage.miniMessage().deserialize("<!i><gray>→ "+BazaarEnum.FARMING_CATEG.get()+" <gray>←")
                ));
                case MINING_CATEG -> meta.lore(List.of(
                        MiniMessage.miniMessage().deserialize("")
                        ,MiniMessage.miniMessage().deserialize("<!i><gray>→ "+BazaarEnum.MINING_CATEG.get()+" <gray>←")
                ));
            }

            item.setItemMeta(meta);
            return item;
        }
        /// Sell All Inventory ; Chest
        private static ItemStack getSellAllInvItem(){
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize(""));

            item.setItemMeta(meta);
            return item;
        }
        /// Sell All Stash ; Bundle
        private static ItemStack getSellAllStashItem(){
            ItemStack item = new ItemStack(Material.BUNDLE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize(""));

            item.setItemMeta(meta);
            return item;
        }
        /// Order / Nota ; Writable book/book&quil
        private static ItemStack getNotaItem(){
            ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize(""));

            item.setItemMeta(meta);
            return item;
        }
        private static ItemStack makeCusItem(Material m, String dispname,List<Component> lore){
            ItemStack item = new ItemStack(m);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize(dispname));
            if(lore != null) meta.lore(lore);

            item.setItemMeta(meta);
            return item;
        }

        private static ItemStack getSlimefunCategItem(boolean showGlint){
            ItemStack item = new ItemStack(Material.MAGMA_CREAM);
            ItemMeta meta = item.getItemMeta();
            if(showGlint) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }else{
                PersistentDataContainer mainTag = meta.getPersistentDataContainer();
                PersistentDataContainer branchMainTag = mainTag.getAdapterContext().newPersistentDataContainer();
                branchMainTag.set(JKey.bazaar_Action, PersistentDataType.STRING, "Categ:SF");
                mainTag.set(JKey.bazaar_Item_GUI, PersistentDataType.TAG_CONTAINER, branchMainTag);
            }
            meta.displayName(MiniMessage.miniMessage().deserialize((String) BazaarEnum.SLIMEFUN_CATEG.get()));
            meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("")
                    ,MiniMessage.miniMessage().deserialize("<!i>"+BazaarEnum.CLICK_TEXT.get()+"<gray> to select this category")
            ));

            item.setItemMeta(meta);
            return item;
        }
        private static ItemStack getMobLootCategItem(boolean showGlint){
            ItemStack item = new ItemStack(Material.ROTTEN_FLESH);
            ItemMeta meta = item.getItemMeta();
            if(showGlint) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }else{
                PersistentDataContainer mainTag = meta.getPersistentDataContainer();
                PersistentDataContainer branchMainTag = mainTag.getAdapterContext().newPersistentDataContainer();
                branchMainTag.set(JKey.bazaar_Action, PersistentDataType.STRING, "Categ:MobLoot");
                mainTag.set(JKey.bazaar_Item_GUI, PersistentDataType.TAG_CONTAINER, branchMainTag);
            }
            meta.displayName(MiniMessage.miniMessage().deserialize((String) BazaarEnum.MOB_LOOT_CATEG.get()));
            meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("")
                    ,MiniMessage.miniMessage().deserialize("<!i>"+BazaarEnum.CLICK_TEXT.get()+"<gray> to select this category")
            ));

            item.setItemMeta(meta);
            return item;
        }
        private static ItemStack getFarmingCategItem(boolean showGlint){
            ItemStack item = new ItemStack(Material.IRON_HOE);
            ItemMeta meta = item.getItemMeta();
            if(showGlint) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }else{
                PersistentDataContainer mainTag = meta.getPersistentDataContainer();
                PersistentDataContainer branchMainTag = mainTag.getAdapterContext().newPersistentDataContainer();
                branchMainTag.set(JKey.bazaar_Action, PersistentDataType.STRING, "Categ:Farming");
                mainTag.set(JKey.bazaar_Item_GUI, PersistentDataType.TAG_CONTAINER, branchMainTag);
            }
            meta.displayName(MiniMessage.miniMessage().deserialize((String) BazaarEnum.FARMING_CATEG.get()));
            meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("")
                    ,MiniMessage.miniMessage().deserialize("<!i>"+BazaarEnum.CLICK_TEXT.get()+"<gray> to select this category")
            ));

            item.setItemMeta(meta);
            return item;
        }
        private static ItemStack getMiningCategItem(boolean showGlint){
            ItemStack item = new ItemStack(Material.IRON_PICKAXE);
            ItemMeta meta = item.getItemMeta();
            if(showGlint) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }else{
                PersistentDataContainer mainTag = meta.getPersistentDataContainer();
                PersistentDataContainer branchMainTag = mainTag.getAdapterContext().newPersistentDataContainer();
                branchMainTag.set(JKey.bazaar_Action, PersistentDataType.STRING, "Categ:Mining");
                mainTag.set(JKey.bazaar_Item_GUI, PersistentDataType.TAG_CONTAINER, branchMainTag);
            }
            meta.displayName(MiniMessage.miniMessage().deserialize((String) BazaarEnum.MINING_CATEG.get()));
            meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("")
                    ,MiniMessage.miniMessage().deserialize("<!i>"+BazaarEnum.CLICK_TEXT.get()+"<gray> to select this category")
            ));

            item.setItemMeta(meta);
            return item;
        }
    }
}