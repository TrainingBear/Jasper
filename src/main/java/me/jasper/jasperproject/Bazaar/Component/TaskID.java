package me.jasper.jasperproject.Bazaar.Component;

import lombok.val;
import me.jasper.jasperproject.Bazaar.Bazaar;
import me.jasper.jasperproject.Bazaar.util.InventoryUpdater;
import me.jasper.jasperproject.Bazaar.util.ProductExecutor;
import me.jasper.jasperproject.Bazaar.util.ProductManager;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.SignGUI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class TaskID {
    public static final byte SWAP_CATEGORY = 0;


    public static final byte SEARCH = 1;
    public static final byte CREATE_BUY_ORDER = 2;
    public static final byte OPEN_PRODUCT_MENU = 3;
    public static final byte UNWRAP_GROUP = 4;
    public static final byte MANAGE_ORDER = 5;
    public static final byte SELL_EVERY_ITEM_IN_INVENTORY = 6;
    public static final byte SET_BUY_AMOUNT = 8;
    public static final byte SET_SELL_AMOUNT = 9;
    public static final byte SELL_INVENTORY = 10;

    public static final byte CLOSE = 11;
    public static final byte CREATE_SELL_ORDER = 12;
    public static final byte CATEG_NAV_NEXT = 13;
    public static final byte CATEG_NAV_BACK = 14;

    public static final byte FILL_INVENTORY = 15;


    public final static Map<Byte, InventoryUpdater> MAP;
    static {
        MAP = new HashMap<>();
        MAP.put(SWAP_CATEGORY,
                (p , inv, ID) -> SwapCategory(inv,ID));
        MAP.put(CLOSE,
                (p , inv, ID) -> p.closeInventory());
        MAP.put(SEARCH,
                (p,inv,ID)-> {
                    String[] builtInText= {
                            ""
                            ,"^^^^^^^^^^^^"
                            ,"Search items"
                            ,""
                    };
                    SignGUI.getInstance().open(p,builtInText, Material.ACACIA_SIGN
                            ,(player, lines, signLoc) -> {
                                player.sendBlockChange(signLoc, signLoc.getBlock().getBlockData());//turn back to normal
                            });
                });
        MAP.put(CATEG_NAV_NEXT,
                (p , inv, ID)-> {
                val id = inv.getItem(4 + 1).getItemMeta().getPersistentDataContainer()
                        .get(JKey.BAZAAR_COMPONENT_ID, PersistentDataType.INTEGER);
                SwapCategory(inv, id);
        });
        MAP.put(CATEG_NAV_BACK,
                (p , inv, ID)-> {
                    val id = inv.getItem(4 - 1).getItemMeta().getPersistentDataContainer()
                            .get(JKey.BAZAAR_COMPONENT_ID, PersistentDataType.INTEGER);
                    SwapCategory(inv, id);
                });
        MAP.put(UNWRAP_GROUP,
                (p, inv, id) -> {
                    invokeSubcategory(p, id, inv);
                });
       
    }
    public static final Map<Byte, ProductExecutor> PRODUCT_MAP;
    static {
        PRODUCT_MAP = new HashMap<>();
        
        PRODUCT_MAP.put(OPEN_PRODUCT_MENU, TaskID::openProductMenu);
        
        PRODUCT_MAP.put(SELL_INVENTORY, (p, i, n) -> Product.fromString(n).instantSell(p));
        PRODUCT_MAP.put(FILL_INVENTORY, (p, i, n) -> Product.fromString(n).instantBuy(p));
        PRODUCT_MAP.put(SET_BUY_AMOUNT, null);
        PRODUCT_MAP.put(SET_SELL_AMOUNT, null);
        PRODUCT_MAP.put(CREATE_BUY_ORDER, null);
        PRODUCT_MAP.put(CREATE_SELL_ORDER, null);
    }

    public static void openProductMenu(Player player, Inventory inventory, String product_name){
        Map<String, Product> products = ProductManager.getProductMap();
        Product product = products.get(product_name);

        int[] sell_border = {19, 21, 37, 38, 39};
        int[] buy_border = {23, 25, 41, 42, 43};
        ItemStack sellb = Bazaar.SELL_BORDER.getItem();
        ItemStack buyb = Bazaar.BUY_BORDER.getItem();
        for (int i : sell_border) inventory.setItem(i, sellb);
        for (int i : buy_border) inventory.setItem(i, buyb);

        inventory.setItem(22, Bazaar.BORDER.getItem());
        inventory.setItem(31, Bazaar.BORDER.getItem());
        inventory.setItem(40, Bazaar.BORDER.getItem());

        ///         SELL SELECTION
        ItemStack SELL_INVENTORY = Bazaar.SELL_INVENTORY.getItem().clone();
        SELL_INVENTORY.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        ItemStack CREATE_SELL_ORDER = Bazaar.CREATE_SELL_ORDER.getItem().clone();
        CREATE_SELL_ORDER.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        ItemStack SELL_AMOUNT = Bazaar.SELL_AMOUNT.getItem().clone();
        SELL_AMOUNT.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        inventory.setItem(20, Bazaar.SELL_DECO.getItem());
        inventory.setItem(28, SELL_AMOUNT);
        inventory.setItem(29, SELL_INVENTORY);
        inventory.setItem(30, CREATE_SELL_ORDER);

        ///         BUY SELECTION
        ItemStack FILL_INVENTORY = Bazaar.FILL_INVENTORY.getItem().clone();
        FILL_INVENTORY.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        ItemStack CREATE_BUY_ORDER = Bazaar.CREATE_BUY_ORDER.getItem().clone();
        CREATE_BUY_ORDER.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        ItemStack BUY_AMOUNT = Bazaar.BUY_AMOUNT.getItem().clone();
        BUY_AMOUNT.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        inventory.setItem(24, Bazaar.BUY_DECO.getItem());
        inventory.setItem(32, BUY_AMOUNT);
        inventory.setItem(33, FILL_INVENTORY);
        inventory.setItem(34, CREATE_BUY_ORDER);

    }

    private static void invokeSubcategory(Player p, final int id, @NotNull Inventory inventory){
        int[] indexes = {
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
        val group_name = Bazaar.getGroupsMap_Int().get(id).getName();
        List<Product> products = ProductManager.getGroupedProduct().get(group_name);
        ItemStack item = Bazaar.getGroupsMap_String().get(group_name).getItem().clone();
        item.editMeta(e->{
            ItemMeta itemMeta = inventory.getItem(13).getItemMeta();
            e.displayName(itemMeta.displayName());
            e.lore(itemMeta.lore());
        });
        inventory.setItem(13, item);

        Iterator<Product> productIterator = products.iterator();
        for (int i : indexes) {
            inventory.setItem(i, null);
            if(productIterator.hasNext()) inventory.setItem(i, productIterator.next().getItem());
        }
    }

    public static void SwapCategory(Inventory inv ,int ID) {
        List<Content> contents = Bazaar.getCategories();
        Content selected_content = null;

        for (Content content : contents) {
            if(content.getID()==ID){
                byte index = (byte) contents.indexOf(content);
                selected_content = contents.get(index);
                for (byte i = 0; i < 5; i++) inv.setItem(i + 2, contents.get(
                        (index + (i - 2) + contents.size()) % contents.size()
                ).getItem());
                break;
            }
        }

        TaskID.invokeCategory(selected_content.getID(), inv);
        TaskID.UpdateDecoration(selected_content, inv);
    }

    public static void UpdateDecoration(Content selectedItem, Inventory inventory){
        int[] indexSlots = {

                18,                  26,
                27,                  35,
                36,                  44,
                    47, 48,   50,51,52,
        };
        ItemStack item = Bazaar.POINTER.getItem().clone();
        item.editMeta(meta -> {
            meta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("")
                    ,MiniMessage.miniMessage().deserialize("<!i><gray><b>▷</b> "
                            +MiniMessage.miniMessage().serialize(selectedItem.getItem().getItemMeta().displayName())
                            +" <gray><b>◁")
            ));
        });
        inventory.setItem(13, item);

        ItemStack selectedItemStack = selectedItem.getItem().clone();
        selectedItemStack.editMeta(selectedItemmeta ->{
            selectedItemmeta.setEnchantmentGlintOverride(true);
            selectedItemmeta.lore(List.of(
                    MiniMessage.miniMessage().deserialize("")
                    ,MiniMessage.miniMessage().deserialize("<!i><color:#77aa77>Selected")
            ));
        });
        inventory.setItem(4, selectedItemStack);

        for (int i : indexSlots) {
            inventory.setItem(
                    i,
                    Bazaar.getDecorationMap().getOrDefault(selectedItem.getID(), Bazaar.getDecorationMap().get(0)).getItem()
            );
        }



    }

    public static void invokeCategory(int ID, Inventory inventory){
        int[] indexes = {
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
        List<Category> SubCategory = Bazaar.getSubCategoriesMap().getOrDefault(ID, null);
        if(SubCategory==null) return;
        Iterator<Category> iterator = SubCategory.iterator();
        for (int index : indexes) {
            inventory.setItem(index, null);
            if(iterator.hasNext()) inventory.setItem(index, iterator.next().getItem());
        }
    }
}
