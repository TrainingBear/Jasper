package me.jasper.jasperproject.Bazaar.Component;

import lombok.Getter;
import lombok.val;
import me.jasper.jasperproject.Bazaar.Bazaar;
import me.jasper.jasperproject.Bazaar.util.InventoryConsumer;
import me.jasper.jasperproject.Bazaar.util.ProductExecutor;
import me.jasper.jasperproject.Bazaar.util.ProductManager;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.SignGUI;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
    public static final byte EXECUTE_SELL_ORDER = 7;
    public static final byte EXECUTE_BUY = 8;
    public static final byte SELL_INVENTORY = 10;

    public static final byte CLOSE = 11;
    public static final byte CREATE_SELL_ORDER = 12;
    public static final byte CATEG_NAV_NEXT = 13;
    public static final byte CATEG_NAV_BACK = 14;

    public static final byte FILL_INVENTORY = 15;
    public static final byte SELL_MINOR_PRICE = 16;
    public static final byte SELL_MAJOR_PRICE = 17;
    public static final byte EXECUTE_SELL = 18;
    public static final byte BUY_MINOR_PRICE = 19;
    public static final byte BUY_MAJOR_PRICE = 20;

    public static final byte OPEN_INSTANT_SELL = 22;
    public static final byte OPEN_INSTANT_BUY = 23;
    public static final byte OPEN_BUY_ORDER_SECTION= 24;
    public static final byte OPEN_SELL_ORDER_SECTION= 25;
    public static final byte OPEN_BAZAAR = 26;
    public static final byte CUSTOM_AMOUNT = 27;
    public static final byte EXECUTE_BUY_ORDER = 28;


    @Getter private static Map<UUID, Inventory> player_order_tab = new HashMap<>();
    public final static Map<Byte, InventoryConsumer> MAP;
    static {
        MAP = new HashMap<>();
        MAP.put(SWAP_CATEGORY,
                (p , inv, ID) -> SwapCategory(inv,ID.getPersistentDataContainer().get(JKey.BAZAAR_COMPONENT_ID, PersistentDataType.INTEGER)));
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
                    invokeSubcategory(p, id.getPersistentDataContainer().get(JKey.BAZAAR_COMPONENT_ID, PersistentDataType.INTEGER), inv);
                });
        MAP.put(OPEN_BAZAAR, (a,b,c)->Bazaar.open(a));
        MAP.put(CUSTOM_AMOUNT, (a, b, c) ->{
            String[] text = {
                    "",
                    "SET AMMOUNT"
            };
            SignGUI.getInstance().open(a, text, Material.OAK_SIGN, (player, lines, signLoc) -> {
                try{
                    int amount = Integer.parseInt(lines[0]);
                    c.getItemMeta().getPersistentDataContainer().set(JKey.BAZAAR_CUSTOM_AMOUNT, PersistentDataType.INTEGER, amount);
                    a.sendMessage("amount has been set to "+amount);
                    c.editMeta(e->{
                        e.displayName(Component.text(amount));
                    });
                    player.openInventory(player_order_tab.get(player.getUniqueId()));
                } catch (NumberFormatException e) {
                    a.sendMessage(e.getMessage());
                }
            });
        });
       
    }
    public static final Map<Byte, ProductExecutor> PRODUCT_MAP;
    static {
        PRODUCT_MAP = new HashMap<>();
        
        PRODUCT_MAP.put(OPEN_PRODUCT_MENU, TaskID::openProductMenu);
        
        PRODUCT_MAP.put(SELL_INVENTORY, (p, i, n) -> Product.fromString(n).instantSell(p));
        PRODUCT_MAP.put(FILL_INVENTORY, (p, i, n) -> Product.fromString(n).instantBuy(p));
        PRODUCT_MAP.put(EXECUTE_BUY_ORDER, null);
        PRODUCT_MAP.put(EXECUTE_SELL_ORDER, null);
        PRODUCT_MAP.put(OPEN_BUY_ORDER_SECTION, TaskID::openBuyOrderSection);
        PRODUCT_MAP.put(OPEN_SELL_ORDER_SECTION, TaskID::openSellOrderSection);
        PRODUCT_MAP.put(OPEN_INSTANT_SELL, TaskID::openSellSection);
        PRODUCT_MAP.put(OPEN_INSTANT_BUY, TaskID::openBuySection);
    }


    public static void openProductMenu(Player player, Inventory inventory, final String product_name){
        Map<String, Product> products = ProductManager.getProductMap();
        Product product = products.get(product_name);
        val current_category_id = inventory.getItem(4).getPersistentDataContainer().get(JKey.BAZAAR_COMPONENT_ID, PersistentDataType.INTEGER);
        player.sendMessage("You invoked "+product_name);

        int[] sell_border = {19, 20, 21, 37, 38, 39};
        int[] buy_border = {23, 24, 25, 41, 42, 43};
        ItemStack sellb = COMP.SELL_BORDER.getItem();
        ItemStack buyb = COMP.BUY_BORDER.getItem();
        for (int i : sell_border) inventory.setItem(i, sellb);
        for (int i : buy_border) inventory.setItem(i, buyb);

        ItemStack theme = Bazaar.getDecorationMap().get(current_category_id).getItem();
        inventory.setItem(22, theme);
        inventory.setItem(30, theme);
        inventory.setItem(32, theme);
        inventory.setItem(40, theme);

        ///         SELL SELECTION
        ItemStack INSTANT_SELL = COMP.INSTANT_SELL.getItem().clone();
        INSTANT_SELL.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        ItemStack CREATE_SELL_ORDER = COMP.OPEN_SELL_ORDER.getItem().clone();
        CREATE_SELL_ORDER.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        inventory.setItem(28, CREATE_SELL_ORDER);
        inventory.setItem(29, INSTANT_SELL);

        ///         BUY SELECTION
        ItemStack INSTANT_BUY = COMP.INSTANT_BUY.getItem().clone();
        INSTANT_BUY.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        ItemStack CREATE_BUY_ORDER = COMP.OPEN_BUY_ORDER.getItem().clone();
        CREATE_BUY_ORDER.editMeta(e -> e.getPersistentDataContainer().set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name));
        inventory.setItem(33, INSTANT_BUY);
        inventory.setItem(34, CREATE_BUY_ORDER);

        inventory.setItem(31, product.getPrototype());
    }

    private static void openBuyOrderSection(Player player, Inventory inventory, String product_name){
        Map<String, Product> products = ProductManager.getProductMap();
        Product product = products.get(product_name);
        ItemStack display = product.getPrototype().clone();
        display.lore().addAll(List.of(
                Util.deserialize("Click to buy item")
        ));
        display.getItemMeta().getPersistentDataContainer().set(JKey.BAZAAR_COMPONENT_TASK_ID, PersistentDataType.BYTE, EXECUTE_BUY_ORDER);

        Inventory menu = Bukkit.createInventory(player, 36, "BUY ORDER");
        menu.setItem(27, COMP.DOOR.getItem());
        menu.setItem(35, COMP.CLOSE.getItem());

        menu.setItem(10, COMP.AMOUNT.getItem());
        menu.setItem(12, display);
        menu.setItem(14, COMP.ORDER_BUY_MINOR_PRICE.getItem());
        menu.setItem(15, COMP.ORDER_BUY_MAJOR_PRICE.getItem());
        menu.setItem(16, COMP.ORDER_BUY_CREATE_PRICE.getItem());

        int[] borders = {0,1,2,3,4,5,6,7,8,9,11,13,17,18,19,20,21,22,23,24,25,26,28,29,30,31,32,33,34};
        ItemStack border = COMP.BUY_BORDER.getItem();
        for (int i : borders)  menu.setItem(i, border);

        player_order_tab.put(player.getUniqueId(), menu);
        player.openInventory(menu);
    }
    private static void openSellOrderSection(Player player, Inventory inventory, String product_name){
        Map<String, Product> products = ProductManager.getProductMap();
        Product product = products.get(product_name);
        ItemStack display = product.getPrototype().clone();
        display.lore().addAll(List.of(
                Util.deserialize("Click to sell item")
        ));
        display.getItemMeta().getPersistentDataContainer().set(JKey.BAZAAR_COMPONENT_TASK_ID, PersistentDataType.BYTE, EXECUTE_BUY_ORDER);

        Inventory menu = Bukkit.createInventory(player, 36, "SELL ORDER");
        menu.setItem(27, COMP.DOOR.getItem());
        menu.setItem(35, COMP.CLOSE.getItem());

        menu.setItem(10, COMP.AMOUNT.getItem());
        menu.setItem(12, display);
        menu.setItem(14, COMP.ORDER_SELL_MINOR_PRICE.getItem());
        menu.setItem(15, COMP.ORDER_SELL_MAJOR_PRICE.getItem());
        menu.setItem(16, COMP.ORDER_SELL_CREATE_PRICE.getItem());

        int[] borders = {0,1,2,3,4,5,6,7,8,9,11,13,17,18,19,20,21,22,23,24,25,26,28,29,30,31,32,33,34};
        ItemStack border = COMP.SELL_BORDER.getItem();
        for (int i : borders)  menu.setItem(i, border);

        player_order_tab.put(player.getUniqueId(), menu);
        player.openInventory(menu);
    }
    private static void openSellSection(Player player, Inventory inventory, String product_name){
        Product product = ProductManager.getProductMap().get(product_name);
        Inventory menu = Bukkit.createInventory(player, 36, "SELL");
        int[] borders = {0,1,2,3,4,5,6,7,8,9,15,17,18,19,20,21,22,23,24,25,26,28,29,30,31,32,33,34};
        ItemStack border = COMP.SELL_BORDER.getItem();
        for (int i : borders)  menu.setItem(i, border);
        menu.setItem(27, COMP.DOOR.getItem());
        menu.setItem(35, COMP.CLOSE.getItem());
        ItemStack one = COMP.FINAL_AMOUNT.getItem().clone();
        one.setAmount(1);
        menu.setItem(10, one);
        ItemStack sixteen = COMP.FINAL_AMOUNT.getItem().clone();
        one.setAmount(16);
        menu.setItem(11, sixteen);
        ItemStack half_stack = COMP.FINAL_AMOUNT.getItem().clone();
        one.setAmount(32);
        menu.setItem(12, half_stack);
        ItemStack astack = COMP.FINAL_AMOUNT.getItem().clone();
        one.setAmount(64);
        menu.setItem(13, astack);
        menu.setItem(14, COMP.AMOUNT.getItem());
        ItemStack prototype = product.getPrototype().clone();
        prototype.editMeta(e->{
        });
        menu.setItem(16, prototype);

        player_order_tab.put(player.getUniqueId(), menu);
        player.openInventory(menu);
    }
    private static void openBuySection(Player player, Inventory inventory, String product_name){
        Product product = ProductManager.getProductMap().get(product_name);
        Inventory menu = Bukkit.createInventory(player, 36, "BUY");
        int[] borders = {0,1,2,3,4,5,6,7,8,9,15,17,18,19,20,21,22,23,24,25,26,28,29,30,31,32,33,34};
        ItemStack border = COMP.BUY_BORDER.getItem();
        for (int i : borders)  menu.setItem(i, border);
        menu.setItem(27, COMP.DOOR.getItem());
        menu.setItem(35, COMP.CLOSE.getItem());
        ItemStack one = COMP.FINAL_AMOUNT.getItem().clone();
        one.setAmount(1);
        menu.setItem(10, one);
        ItemStack sixteen = COMP.FINAL_AMOUNT.getItem().clone();
        one.setAmount(16);
        menu.setItem(11, sixteen);
        ItemStack half_stack = COMP.FINAL_AMOUNT.getItem().clone();
        one.setAmount(32);
        menu.setItem(12, half_stack);
        ItemStack astack = COMP.FINAL_AMOUNT.getItem().clone();
        one.setAmount(64);
        menu.setItem(13, astack);
        menu.setItem(14, COMP.AMOUNT.getItem());
        ItemStack prototype = product.getPrototype().clone();
        prototype.editMeta(e->{
        });
        menu.setItem(16, prototype);

        player_order_tab.put(player.getUniqueId(), menu);
        player.openInventory(menu);
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
        ItemStack item = COMP.POINTER.getItem().clone();
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
