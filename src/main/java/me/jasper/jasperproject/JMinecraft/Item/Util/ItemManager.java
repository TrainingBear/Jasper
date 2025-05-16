package me.jasper.jasperproject.JMinecraft.Item.Util;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTFile;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import lombok.Getter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities.*;
import me.jasper.jasperproject.JMinecraft.Item.Product.Series.Titanium.*;
import me.jasper.jasperproject.JMinecraft.Item.Product.Weapons.*;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchant;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Enchants.Sharpness;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JMinecraft.Item.JItem;
import me.jasper.jasperproject.JMinecraft.Item.Product.Tools.*;
import me.jasper.jasperproject.JMinecraft.Item.Product.Utilities.*;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public final class ItemManager {
    private static final PluginManager pluginManager = JasperProject.getPM();
    private static final Plugin plugin = JasperProject.getPlugin();

    @Getter private static final HashSet<ItemAbility> abilities = new HashSet<>();
    @Getter private static final HashMap<String, JItem> items = new HashMap<>();
    @Getter private static final HashSet<Enchant> enchants = new HashSet<>();

    private static void registerItem(Factory... factory){
        plugin.getLogger().info("Registering Items...");
        List<String> itemList = new ArrayList<>();
        for(Factory fac : factory){
            JItem item = fac.finish();
            items.put(item.getID().toUpperCase(), item);
            itemList.add(item.getID());
        }
        plugin.getLogger().info("Registered: "+String.join(", ",itemList));
    }
    private static void registerAbility(ItemAbility... ability){
        plugin.getLogger().info("Registering Abilities...");
        for(ItemAbility abilti : ability){
            pluginManager.registerEvents(abilti, plugin);
            abilities.add(abilti);
        }
    }

    private static void registerEnchant(Enchant enchant){
        plugin.getLogger().severe("Registering Enchants...");
        pluginManager.registerEvents(enchant, plugin);
        enchants.add(enchant);
    }

    public static void registerAll(){
        registerAbility(
                Teleport.getInstance(),
                Grappling_Hook.getInstance(),
                Warper.getInstance(),
                Animator.getInstance(),
                Burst_Arrow.getInstance(),
                Heal.getInstance(),
                Jumper.getInstance(),
                BackStab.getInstance(),
                Burnt.getInstance(),
                Bash.getInstance(),
                Plower.getInstance()
        );

        registerItem(
                new Blender(),
                new EndGateway(),
                new Warp_Gateway(),
                new GraplingHook(),
                new TestItem(),
                new Titanium_Sword(),
                new Burst_Bow(),
                new Healing_Wand(),
                new Healing_Staff(),
                new Titanium_Pickaxe(),
                new FeatherJumper(),
                new AssassinDagger(),
                new Stack_Steels(),
                new Heavy_Axe(),
                new Test_Bow(),

                new Titanium_Shovel(),
                new Titanium_Helmet(),
                new Titanium_Chestplate(),
                new Titanium_Leggings(),
                new Titanium_Boots(),

                new Farmer_Hoe(),
                new Farmer_Scythe(),
                new Advanced_Hoe()
        );

        registerEnchant(new Sharpness());
    }

    public static void runUpdater() {

        File file = new File(Bukkit.getWorld("world").getWorldFolder().getAbsolutePath()+"\\playerdata");
        JasperProject plugin = JasperProject.getPlugin();
        File[] files = file.listFiles();

        long startTime = System.nanoTime();
        int total_item_updated = 0;
        for (File playerData : files) {
            long startTime2 = System.nanoTime();
            if(!playerData.getName().endsWith(".dat")) continue;
            UUID id = UUID.fromString(playerData.getName().substring(0, 36));
            Player player = Bukkit.getPlayer(id);


            try{
                NBTFile playernbt = new NBTFile(playerData);
                NBTCompoundList inventory = playernbt.getCompoundList("Inventory");
                for (ReadWriteNBT item : inventory) {
                    if(!hasCustomID(item)) continue;
                    if(!items.containsKey(getID(item))) continue;
                    JItem new_version = items.get(getID(item).toUpperCase());
                    if(getVersion(item) == new_version.getVersion()) continue;
                    if(player!=null){
                        player.sendMessage("pergi sana!");
                        player.kickPlayer("Detected new item version in your inventory! Updating item, please wait!");
                        player.saveData();
                    }
                    plugin.getLogger().info("[ItemPatcher] "+getID(item)+" Detected new Version! of "+getVersion(item) +" -> "+new_version.getVersion());
                    NBTCompound nbt_item = (NBTCompound) item;
                    ItemStack current = NBTItem.convertNBTtoItem(nbt_item);//ReadWriteNBT -> ItemStack
                    ItemStack patched_item = JItem.convertFrom(current).patch(new_version).getItem();
                    item.mergeCompound(NBTItem.convertItemtoNBT(patched_item));
                    total_item_updated++;
                }
                playernbt.save();
                String time2 = String.format("%.2f", (System.nanoTime() - startTime2) / 1_000_000.0);
                plugin.getLogger().info("[ItemPatcher] Updating "+playernbt.getCompound("bukkit").getString("lastKnownName")+" inventory took "+time2+"ms");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        String time2 = String.format("%.2f", (System.nanoTime() - startTime) / 1_000_000.0);
        plugin.getLogger().info("[ItemPatcher] "+total_item_updated+" Items have been updated! ("+time2+"ms)");
    }

    private static long getVersion(ReadWriteNBT item){
        return getBukkitValues(item).getLong(JKey.Version.toString());
    }
    private static String getID(ReadWriteNBT item){
        return getBukkitValues(item).getString(JKey.JasperItem.toString());
    }
    private static boolean hasCustomID(ReadWriteNBT item){
        return item.hasTag("components") &&
                item.getCompound("components").hasTag("minecraft:custom_data") &&
                item.getCompound("components").getCompound("minecraft:custom_data").hasTag("PublicBukkitValues");
    }
    private static ReadWriteNBT getBukkitValues(ReadWriteNBT item){
        return item.getCompound("components").getCompound("minecraft:custom_data")
                .getCompound("PublicBukkitValues");
    }
}
