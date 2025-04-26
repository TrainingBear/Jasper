package me.jasper.jasperproject.Bazaar;

import lombok.Getter;
import me.jasper.jasperproject.Bazaar.Component.COMP;
import me.jasper.jasperproject.Bazaar.Component.Category;
import me.jasper.jasperproject.Bazaar.Component.TaskID;
import me.jasper.jasperproject.Bazaar.anotation.BazaarComponent;
import me.jasper.jasperproject.Bazaar.anotation.CategoryType;
import me.jasper.jasperproject.Bazaar.util.BazaarEnum;
import me.jasper.jasperproject.Util.ContainerMenu.Border;
import me.jasper.jasperproject.Util.ContainerMenu.Container;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import me.jasper.jasperproject.Util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Bazaar {
    private static final AtomicInteger atom = new AtomicInteger(1);
    public static int id(){
        return atom.getAndIncrement();
    }
    @Deprecated
    public static void init(){
        updateGroups();
        update_group_map();
    }

    @BazaarComponent(name = "Category Component")
    private static final Category slimefun = new Category(id(), Material.MAGMA_CREAM, Util.deserialize((String) BazaarEnum.SLIMEFUN_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category mob_loot = new Category(id(), Material.ROTTEN_FLESH, Util.deserialize((String) BazaarEnum.MOB_LOOT_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category farming = new Category(id(), Material.WHEAT, Util.deserialize((String)BazaarEnum.FARMING_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category mining = new Category(id(), Material.GOLDEN_PICKAXE, Util.deserialize((String) BazaarEnum.MINING_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category fishing = new Category(id(), Material.AXOLOTL_BUCKET, Util.deserialize((String) BazaarEnum.FISHING_CATEG.get()), TaskID.SWAP_CATEGORY);
    private static final Category magic = new Category(id(), Material.GLOWSTONE_DUST, Util.deserialize((String) BazaarEnum.MAGICAL_CATEG.get()), TaskID.SWAP_CATEGORY);
    @Getter private final static List<Content> Categories = List.of(
        slimefun, mob_loot, farming, mining, fishing, magic
    );

    @BazaarComponent(name = "Sub-Category Component")

    @CategoryType(name = "mob_loot")
    private static final Category monster_mob_loot = new Category(id(), Material.ROTTEN_FLESH, Util.deserialize("<!i>Monster Drop"), TaskID.UNWRAP_GROUP);
//    private static final Category animal_mob_loot =
//    private static final Category slimefun_mob_loot =
//    private static final Category other_mob_loot =

    @CategoryType(name = "farming")
    private static final Category farm_farming = new Category(id(), Material.WHEAT, Util.deserialize("<!i>Farming commodity"), TaskID.UNWRAP_GROUP);
    private static final Category seed_farming = new Category(id(), Material.WHEAT_SEEDS, Util.deserialize("<!i>Seeds"), TaskID.UNWRAP_GROUP);
    private static final Category wood_farming = new Category(id(), Material.OAK_LOG, Util.deserialize("<!i>Woods"), TaskID.UNWRAP_GROUP);
    private static final Category garden_farming =new Category(id(), Material.OAK_LOG, Util.deserialize("<!i>Garden"), TaskID.UNWRAP_GROUP);

    @CategoryType(name = "slimefun")
    private static final Category dust_slimefun = new Category(id(), Material.GLOWSTONE_DUST, Util.deserialize("<!i>Dust"), TaskID.UNWRAP_GROUP);
    private static final Category machine_slimefun = new Category(id(), Material.FURNACE, Util.deserialize("<!i>Machine"), TaskID.UNWRAP_GROUP);
    private static final Category magic_slimefun = new Category(id(), Material.MAGENTA_DYE, Util.deserialize("<!i>Magic"), TaskID.UNWRAP_GROUP);
    private static final Category cargo_slimefun = new Category(id(), Material.LIGHTNING_ROD, Util.deserialize("<!i>Cargo"), TaskID.UNWRAP_GROUP);

    @CategoryType(name = "mining")
    private static final Category mineral_mining = new Category(id(), Material.DIAMOND, Util.deserialize("<!i>Minerals"), TaskID.UNWRAP_GROUP);
    private static final Category stone_mining = new Category(id(), Material.COBBLESTONE, Util.deserialize("<!i>Stone"), TaskID.UNWRAP_GROUP);
//    private static final Category _mining = new Category(id(), Material.COBBLESTONE, Util.deserialize("<!i>Stone"), TaskID.UNWRAP_GROUP);

    @CategoryType(name = "fishing")
    private static final Category fish_fishing = new Category(id(), Material.TROPICAL_FISH, Util.deserialize("<!i>Fish"), TaskID.UNWRAP_GROUP);
    private static final Category loot_fishing = new Category(id(), Material.FISHING_ROD, Util.deserialize("<!i>Fish loot"), TaskID.UNWRAP_GROUP);

    @CategoryType(name = "magical")
    private static final Category _magic = new Category(id(), Material.STRUCTURE_VOID, Util.deserialize("<!i>Magic - soon"), TaskID.UNWRAP_GROUP);

    @Getter private final static Map<Integer, List<Category>> SubCategoriesMap = Map.of(
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


    @Getter private static final List<Category> SubCategories;
    static {
        SubCategories = new ArrayList<>();
        for (List<Category> value : SubCategoriesMap.values()) {
            SubCategories.addAll(value);
        }
    }

    @Getter private static final Map<String, Category> groupsMap_String = new HashMap<>();
    public static void updateGroups(){
        for (Category content : SubCategories) {
            groupsMap_String.put(content.getName(), content);
        }
    }

    @Getter private static Map<Integer, Category> groupsMap_Int = new HashMap<>();
    public static void update_group_map(){
        for (Category content : SubCategories) {
            groupsMap_Int.put(content.getID(), content);
        }
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
        Inventory inv = INSTANCE(player);
        player.openInventory(inv);
    }


    private static final Map<UUID, Container> instances = new HashMap<>();

    private static Inventory INSTANCE(@NotNull Player player){
        Container container;
        if(!instances.containsKey(player.getUniqueId())){
            container = new Container(player, Util.deserialize(BazaarEnum.TITLE_STRING.get()+" <yellow>Worldwide bazaar"), 54);
            container.load(()->{
                int def = mining.getID();
                TaskID.SwapCategory(container.getContainer(), def);
                initLayout(container.getContainer());
            });
        } else {
            container = null;
        }
        return instances.computeIfAbsent(player.getUniqueId(), k -> container).getContainer();
    }

    private static void initLayout(@NotNull Inventory inventory){
        inventory.setItem(53, COMP.CLOSE.getItem());
        inventory.setItem(45, COMP.SELL_INV.getItem());
        inventory.setItem(46, COMP.ORDER.getItem());
        inventory.setItem(49, COMP.SEARCH.getItem());
        inventory.setItem(1, COMP.CATEG_NAVI_BACK.getItem());
        inventory.setItem(7, COMP.CATEG_NAVI_NEXT.getItem());
        int[] borders = {
                0, 8, 9, 10, 11, 12, 14, 15, 16, 17
        };

        for (int i : borders) {
            inventory.setItem(i, COMP.BORDER.getItem());
        }
    }
}
