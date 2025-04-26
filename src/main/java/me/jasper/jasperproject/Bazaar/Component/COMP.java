package me.jasper.jasperproject.Bazaar.Component;

import me.jasper.jasperproject.Bazaar.Bazaar;
import me.jasper.jasperproject.Bazaar.anotation.BazaarComponent;
import me.jasper.jasperproject.Bazaar.util.BazaarEnum;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class COMP {
    private static int id(){
        return Bazaar.id();
    }


    private final static Border BLANK = new Border(-1, Material.AIR, false);
    public final static Border BORDER = new Border(id(), Material.BLACK_STAINED_GLASS_PANE, false);

    public final static Category FINAL_AMOUNT = new Category(id(), Material.NAME_TAG, Util.deserialize("<!i><blue>N/A"), TaskID.EXECUTE_BUY);
    public final static Category DOOR = new Category(id(), Material.OAK_DOOR, Util.deserialize("<!i><blue>BACK TO MAIN MENU"), TaskID.OPEN_BAZAAR);
    public final static Category AMOUNT;
    static{
        AMOUNT = new Category(id(), Material.OAK_SIGN, Util.deserialize("<!i><blue>SEL AMOUNT"), TaskID.CUSTOM_AMOUNT);
        AMOUNT.getItem().getItemMeta().getPersistentDataContainer().set(JKey.BAZAAR_CUSTOM_AMOUNT, PersistentDataType.INTEGER, 1);
    }
    @BazaarComponent(name = "Product Menu")
    public final static Category OPEN_BUY_ORDER = new Category(id(), Material.MOJANG_BANNER_PATTERN, Util.deserialize("<!i><blue>Create Buy Order"), TaskID.OPEN_BUY_ORDER_SECTION);
    public final static Category OPEN_SELL_ORDER = new Category(id(), Material.MOJANG_BANNER_PATTERN, Util.deserialize("<!i><blue>Create Sell Order"), TaskID.OPEN_SELL_ORDER_SECTION);
    public final static Category FILL_INVENTORY = new Category(id(), Material.CHEST, Util.deserialize("<!i><blue>Fill Inventory"), TaskID.FILL_INVENTORY);
    public final static Category SELL_INVENTORY = new Category(id(), Material.CHEST, Util.deserialize("<!i><blue>Sell Inventory"), TaskID.SELL_INVENTORY);
    public final static Category BUY_AMOUNT = new Category(id(), Material.OAK_SIGN, Util.deserialize("<!i><blue>Custom Amount"), TaskID.CUSTOM_AMOUNT);
    public final static Category SELL_AMOUNT = new Category(id(), Material.OAK_SIGN, Util.deserialize("<!i><blue>Custom Amount"), TaskID.CUSTOM_AMOUNT);
    public final static Category INSTANT_BUY = new Category(id(), Material.ITEM_FRAME, Util.deserialize("<!i><blue>INSTANT BUY"), TaskID.OPEN_INSTANT_BUY);
    public final static Category INSTANT_SELL = new Category(id(), Material.HOPPER, Util.deserialize("<!i><blue>INSTANT SELL"), TaskID.OPEN_INSTANT_SELL);
    public final static Border BUY_DECO = new Border(id(), Material.GOLD_BLOCK, true , Util.deserialize("<!i><blue>Beli"), false);
    public final static Border SELL_DECO = new Border(id(), Material.DIAMOND_BLOCK, true , Util.deserialize("<!i><gold>Jual"), false);
    public final static Border SELL_BORDER = new Border(id(), Material.RED_STAINED_GLASS_PANE, false , Util.deserialize("<!i><red>Tempat Menjual Barang"), false);
    public final static Border BUY_BORDER = new Border(id(), Material.GREEN_STAINED_GLASS_PANE, false , Util.deserialize("<!i><green>Tempat Membeli Barang"), false);

    @BazaarComponent(name = "Create Order Component")
    public final static Category ORDER_SELL_SET_AMOUNT = new Category(id(), Material.OAK_SIGN, Util.deserialize("<!i><blue>CREATE A PRICE"), TaskID.SELL_MINOR_PRICE);
    public final static Category ORDER_BUY_SET_AMOUNT = new Category(id(), Material.OAK_SIGN, Util.deserialize("<!i><blue>CREATE A PRICE"), TaskID.SELL_MINOR_PRICE);
    public final static Category ORDER_SELL_MINOR_PRICE = new Category(id(), Material.GOLD_NUGGET, Util.deserialize("<!i><blue>+0,1 price from top offer"), TaskID.SELL_MINOR_PRICE);
    public final static Category ORDER_SELL_MAJOR_PRICE = new Category(id(), Material.GOLD_INGOT, Util.deserialize("<!i><blue>+1% price from top offer"), TaskID.SELL_MAJOR_PRICE);
    public final static Category ORDER_SELL_CREATE_PRICE = new Category(id(), Material.GOLDEN_HELMET, Util.deserialize("<!i><blue>CREATE A NEW PRICE FOR THIS ITEM"), TaskID.CUSTOM_AMOUNT);
    public final static Category ORDER_BUY_MINOR_PRICE = new Category(id(), Material.GOLD_NUGGET, Util.deserialize("<!i><blue>-0,1 price from top offer"), TaskID.BUY_MINOR_PRICE);
    public final static Category ORDER_BUY_MAJOR_PRICE = new Category(id(), Material.GOLD_INGOT, Util.deserialize("<!i><blue>-1% price from top offer"), TaskID.BUY_MAJOR_PRICE);
    public final static Category ORDER_BUY_CREATE_PRICE = new Category(id(), Material.GOLDEN_HELMET, Util.deserialize("<!i><blue>CREATE A NEW PRICE FOR THIS ITEM"), TaskID.CUSTOM_AMOUNT);

    @BazaarComponent(name = "Base Component")
    public final static Border POINTER = new Border(id(), Material.LIME_STAINED_GLASS_PANE, false , Util.deserialize("<!i><green>Selected category"), false);
    public final static Category CATEG_NAVI_NEXT = new Category(id(), Material.SPECTRAL_ARROW,Util.deserialize("<!i><color:#dedc7a>Next") , TaskID.CATEG_NAV_NEXT , List.of(Util.deserialize(""),Util.deserialize(BazaarEnum.CLICK_TEXT.get()+" <gray>to select next"),Util.deserialize("<!i><gray>category")));
    public final static Category CATEG_NAVI_BACK = new Category(id(), Material.SPECTRAL_ARROW,Util.deserialize("<!i><color:#dedc7a>Back") ,TaskID.CATEG_NAV_BACK ,List.of(Util.deserialize(""),Util.deserialize(BazaarEnum.CLICK_TEXT.get()+" <gray>to select previous"),Util.deserialize("<!i><gray>category")));
    public final static Category SEARCH = new Category(id(), Material.ACACIA_HANGING_SIGN, (Component) BazaarEnum.TITLE_NAME.get(),TaskID.SEARCH,List.of(
            Util.deserialize("<!i><yellow>Worldwide bazaar"),
            Util.deserialize(""),
            Util.deserialize(BazaarEnum.CLICK_TEXT.get()+" <gray>to search thing")
    ));
    public final static Category CLOSE = new Category(id(), Material.BARRIER , (Component) BazaarEnum.CLOSE_ITEM.get(),TaskID.CLOSE,List.of(
            Util.deserialize(""),
            Util.deserialize(BazaarEnum.CLICK_TEXT.get()+" <gray>to close")
    ));

    @BazaarComponent(name = "Instant Sell Inventory")
    public final static Category SELL_INV = new Category(id(), Material.CHEST,
            Util.deserialize("<!i><gold>Sell items in inventory"),TaskID.SELL_EVERY_ITEM_IN_INVENTORY,List.of(
            Util.deserialize("<!i><gray>Currently there's no"),
            Util.deserialize("<!i><gray>match item in your inventory")

    ));

    @BazaarComponent(name = "Order List")
    public final static Category ORDER = new Category(id(), Material.WRITABLE_BOOK,
            Util.deserialize("<!i><Yellow>Manage Order"),TaskID.MANAGE_ORDER,List.of(
            Util.deserialize("<!i><gray>Currently there's no your order")
    ));
}
