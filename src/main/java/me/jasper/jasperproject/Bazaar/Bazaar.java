package me.jasper.jasperproject.Bazaar;

import lombok.Getter;
import me.jasper.jasperproject.Bazaar.Component.Category;
import me.jasper.jasperproject.Bazaar.Component.TaskID;
import me.jasper.jasperproject.Bazaar.anotation.BazaarComponent;
import me.jasper.jasperproject.Bazaar.anotation.CategoryType;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.ContainerMenu.Container;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Bazaar {
    private static final AtomicInteger atom = new AtomicInteger();
    private static int id(){
        return atom.getAndDecrement();
    }

    @BazaarComponent(name = "Component")
    private final static Border BLANK = new Border(-1, Material.AIR, false);
    private final static Border BORDER = new Border(id(), Material.BLACK_STAINED_GLASS_PANE, false);
    public final static Border POINTER = new Border(id(), Material.LIME_STAINED_GLASS_PANE, false , miniMsgDese("<!i><green>Selected category"), false);
    public final static Category CATEG_NAVI_NEXT = new Category(id(), Material.SPECTRAL_ARROW,miniMsgDese("<!i><color:#dedc7a>Next") , TaskID.CATEG_NAV_NEXT ,List.of(miniMsgDese(""),miniMsgDese(BazaarEnum.CLICK_TEXT.get()+" <gray>to select next"),miniMsgDese("<!i><gray>category")));
    public final static Category CATEG_NAVI_BACK = new Category(id(), Material.SPECTRAL_ARROW,miniMsgDese("<!i><color:#dedc7a>Back") ,TaskID.CATEG_NAV_BACK ,List.of(miniMsgDese(""),miniMsgDese(BazaarEnum.CLICK_TEXT.get()+" <gray>to select previous"),miniMsgDese("<!i><gray>category")));
    private final static Category TITLE_SIGN = new Category(id(), Material.ACACIA_HANGING_SIGN, (Component) BazaarEnum.TITLE_NAME.get(),TaskID.SEARCH,List.of(
                miniMsgDese("<!i><yellow>Worldwide bazaar")
                ,miniMsgDese("")
                ,miniMsgDese(BazaarEnum.CLICK_TEXT.get()+" <gray>to search thing")
    ));
    private final static Category CLOSE = new Category(id(), Material.BARRIER , (Component) BazaarEnum.CLOSE_ITEM.get(),TaskID.CLOSE,List.of(
            miniMsgDese("")
            ,miniMsgDese(BazaarEnum.CLICK_TEXT.get()+" <gray>to close")
    ));

    @BazaarComponent(name = "Instant Sell Inventory")
    private final static Category SELL_INV = new Category(id(), Material.CHEST
            , miniMsgDese("<!i><gold>Sell items in inventory"),TaskID.SELL_INV,List.of(
                miniMsgDese("<!i><gray>Currently there's no")
                ,miniMsgDese("<!i><gray>match item in your inventory")

    ));

    @BazaarComponent(name = "Order List")
    private final static Category NOTA_BOOK = new Category(id(), Material.WRITABLE_BOOK
            , miniMsgDese("<!i><Yellow>Manage Order"),TaskID.MANAGE_ORDER,List.of(
            MiniMessage.miniMessage().deserialize("<!i><gray>Currently there's no your order")
    ));

    @BazaarComponent(name = "Category Component")
    private static final Category slimefun = new Category(id(), Material.MAGMA_CREAM, miniMsgDese((String) BazaarEnum.SLIMEFUN_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category mob_loot = new Category(id(), Material.ROTTEN_FLESH, miniMsgDese((String) BazaarEnum.MOB_LOOT_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category farming = new Category(id(), Material.WHEAT, miniMsgDese((String)BazaarEnum.FARMING_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category mining = new Category(id(), Material.GOLDEN_PICKAXE, miniMsgDese((String) BazaarEnum.MINING_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category fishing = new Category(id(), Material.AXOLOTL_BUCKET, miniMsgDese((String) BazaarEnum.FISHING_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category magic = new Category(id(), Material.GLOWSTONE_DUST, miniMsgDese((String) BazaarEnum.MAGICAL_CATEG.get()), TaskID.SWAP_CATEGORY);
    @Getter private final static List<Content> Categories = List.of(
        slimefun, mob_loot, farming, mining, fishing, magic
    );

    @BazaarComponent(name = "Sub-Category Component")

    @CategoryType(name = "mob_loot")
    private static final Category monster_mob_loot = new Category(id(), Material.ROTTEN_FLESH, miniMsgDese("<!i>Monster Drop"), TaskID.UNWRAP_GROUP);
//    private static final Category animal_mob_loot =
//    private static final Category slimefun_mob_loot =
//    private static final Category other_mob_loot =

    @CategoryType(name = "farming")
    private static final Category farm_farming = new Category(id(), Material.WHEAT, miniMsgDese("<!i>Farming commodity"), TaskID.UNWRAP_GROUP);
    private static final Category seed_farming = new Category(id(), Material.WHEAT_SEEDS, miniMsgDese("<!i>Seeds"), TaskID.UNWRAP_GROUP);
    private static final Category wood_farming = new Category(id(), Material.OAK_LOG, miniMsgDese("<!i>Woods"), TaskID.UNWRAP_GROUP);
    private static final Category garden_farming =new Category(id(), Material.OAK_LOG, miniMsgDese("<!i>Garden"), TaskID.UNWRAP_GROUP);

    @CategoryType(name = "slimefun")
    private static final Category dust_slimefun = new Category(id(), Material.GLOWSTONE_DUST, miniMsgDese("<!i>Dust"), TaskID.UNWRAP_GROUP);
    private static final Category machine_slimefun = new Category(id(), Material.FURNACE, miniMsgDese("<!i>Machine"), TaskID.UNWRAP_GROUP);
    private static final Category magic_slimefun = new Category(id(), Material.MAGENTA_DYE, miniMsgDese("<!i>Magic"), TaskID.UNWRAP_GROUP);
    private static final Category cargo_slimefun = new Category(id(), Material.LIGHTNING_ROD, miniMsgDese("<!i>Cargo"), TaskID.UNWRAP_GROUP);

    @CategoryType(name = "mining")
    private static final Category mineral_mining = new Category(id(), Material.DIAMOND, miniMsgDese("<!i>Minerals"), TaskID.UNWRAP_GROUP);
    private static final Category stone_mining = new Category(id(), Material.COBBLESTONE, miniMsgDese("<!i>Stone"), TaskID.UNWRAP_GROUP);
//    private static final Category _mining = new Category(id(), Material.COBBLESTONE, miniMsgDese("<!i>Stone"), TaskID.UNWRAP_GROUP);

    @CategoryType(name = "fishing")
    private static final Category fish_fishing = new Category(id(), Material.TROPICAL_FISH, miniMsgDese("<!i>Fish"), TaskID.UNWRAP_GROUP);
    private static final Category loot_fishing = new Category(id(), Material.FISHING_ROD, miniMsgDese("<!i>Fish loot"), TaskID.UNWRAP_GROUP);

    @CategoryType(name = "magical")
    private static final Category _magic = new Category(id(), Material.STRUCTURE_VOID, miniMsgDese("<!i>Magic - soon"), TaskID.UNWRAP_GROUP);

    @Getter private final static Map<Integer, List<Category>> SubCategories = Map.of(
            slimefun.getID(), List.of(
                    dust_slimefun,
                    machine_slimefun,
                    magic_slimefun,
                    cargo_slimefun
            )
            , mob_loot.getID(), List.of(
                    monster_mob_loot
            )
            , farming.getID(), List.of(
                    farm_farming,
                    seed_farming,
                    wood_farming,
                    garden_farming
            )
            , mining.getID(), List.of(
                    mineral_mining,
                    stone_mining
            )
            , fishing.getID(), List.of(
                    fish_fishing,
                    loot_fishing
            )
            , magic.getID(), List.of(
                    _magic
            )
    );

    public static @NotNull Set<String> getGroups(){
        Set<String> groups = new HashSet<>();
        for (List<Category> list : SubCategories.values()) {
            for (Category content : list) {
                groups.add(content.getName());
            }
        }
        return groups;
    }

    public static Map<Integer, String> getGroupsID(){
        Map<Integer, String> groups = new HashMap<>();
        for (List<Category> list : SubCategories.values()) {
            for (Category content : list) {
                groups.put(content.getID(), content.getName());
            }
        }
        return groups;
    }

    /**
     *  the list of products is on ProductManager
      */

    @BazaarComponent(name = "Decoration")
    private final static Border CATEG_DEFAULT_DECO = new Border(id(), Material.COBWEB, false);
    private final static Border SLIMEFUN_CATEG_DECO = new Border(id(), Material.LIME_STAINED_GLASS_PANE, false);
    private final static Border MOBLOOT_CATEG_DECO = new Border(id(), Material.ORANGE_STAINED_GLASS_PANE, false);
    private final static Border FARMING_CATEG_DECO = new Border(id(), Material.YELLOW_STAINED_GLASS_PANE, false);
    private final static Border MINING_CATEG_DECO = new Border(id(), Material.LIGHT_BLUE_STAINED_GLASS_PANE, false);
    private final static Border FISHING_CATEG_DECO = new Border(id(), Material.BLUE_STAINED_GLASS_PANE, false);
    private final static Border MAGICAL_CATEG_DECO = new Border(id(), Material.PINK_STAINED_GLASS_PANE, false);
    @Getter private final static Map<Integer, Border> DecorationMap;
    static {
        DecorationMap = Map.of(
                0, CATEG_DEFAULT_DECO
                , slimefun.getID(), SLIMEFUN_CATEG_DECO
                , mob_loot.getID(), MOBLOOT_CATEG_DECO
                , farming.getID(), FARMING_CATEG_DECO
                , mining.getID(), MINING_CATEG_DECO
                , fishing.getID(), FISHING_CATEG_DECO
                , magic.getID(), MAGICAL_CATEG_DECO
        );
    }


    /**
     * MAIN METHOD
     * @param player target
     */
    public static void open(Player player){
        byte slotIndex = 18;
        Inventory inv = INSTANCE(player);
        player.openInventory(inv);
    }

    private static Component miniMsgDese(String s, TagResolver... t){
        return MiniMessage.miniMessage().deserialize(s, t);
    }

    private static final Map<UUID, Container> instances = new HashMap<>();

    private static Inventory INSTANCE(@NotNull Player player){
        Container container;
        if(!instances.containsKey(player.getUniqueId())){
            container = new Container(player, miniMsgDese(BazaarEnum.TITLE_STRING.get()+" <yellow>Worldwide bazaar"), 54);
            container.load(()->{
                TaskID.UpdateDecoration(Categories.get(0), container.getContainer());
                TaskID.UpdateSubcategory(Categories.get(0).getID(), container.getContainer());
                putBorder(container.getContainer());
            });
        } else {
            container = null;
        }
        return instances.computeIfAbsent(player.getUniqueId(), k -> container).getContainer();
    }

    private static void putBorder(Inventory inventory){
        int[] indexes = {
          45, 27, 9, 10, 11, 13, 14, 15, 16, 17, 7
        };

        for (int i : indexes) {
            inventory.setItem(i, BORDER.getItem());
        }
    }

}
