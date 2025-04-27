package me.jasper.jasperproject.JasperItem.Util;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTFile;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import lombok.Getter;
import lombok.val;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities.*;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Enchant;
import me.jasper.jasperproject.JasperItem.ItemAttributes.Enchants.Sharpness;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.JItem;
import me.jasper.jasperproject.JasperItem.Product.Tools.Blender;
import me.jasper.jasperproject.JasperItem.Product.Tools.GraplingHook;
import me.jasper.jasperproject.JasperItem.Product.Tools.Titanium_Pickaxe;
import me.jasper.jasperproject.JasperItem.Product.Utilities.Feather_Jumper;
import me.jasper.jasperproject.JasperItem.Product.Utilities.Healing_Staff;
import me.jasper.jasperproject.JasperItem.Product.Utilities.Healing_Wand;
import me.jasper.jasperproject.JasperItem.Product.Utilities.Stack_Steels;
import me.jasper.jasperproject.JasperItem.Product.Weapons.*;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

@Getter
public final class ItemManager {
    private static final PluginManager pluginManager = JasperProject.getPM();
    private static final Plugin plugin = JasperProject.getPlugin();

    @Getter private static final HashSet<ItemAbility> abilities = new HashSet<>();
    @Getter private static final HashMap<String, JItem> items = new HashMap<>();
    @Getter private static final HashSet<Enchant> enchants = new HashSet<>();

    private static void registerItem(Factory factory){
        JItem item = factory.finish();
        items.put(item.getID().toUpperCase(), item);
    }
    private static void registerAbility(ItemAbility ability){
        pluginManager.registerEvents(ability, plugin);
        abilities.add(ability);
    }

    private static void registerEnchant(Enchant enchant){
        pluginManager.registerEvents(enchant, plugin);
        enchants.add(enchant);
    }

    public static void registerAll(){

        registerAbility(Teleport.getInstance());
        registerAbility(Grappling_Hook.getInstance());
        registerAbility(Warper.getInstance());
        registerAbility(Animator.getInstance());
        registerAbility(Burst_Arrow.getInstance());
        registerAbility(Heal.getInstance());
        registerAbility(Jumper.getInstance());
        registerAbility(BackStab.getInstance());
        registerAbility(Burnt.getInstance());

        registerItem(new Blender());
        registerItem(new End_Gateway());
        registerItem(new Warp_Gateway());
        registerItem(new GraplingHook());
        registerItem(new TestItem());
        registerItem(new Titanium_Sword());
        registerItem(new Burst_Bow());
        registerItem(new Healing_Wand());
        registerItem(new Healing_Staff());
        registerItem(new Titanium_Pickaxe());
        registerItem(new Feather_Jumper());
        registerItem(new Assassin_Dagger());
        registerItem(new Stack_Steels());

        registerEnchant(new Sharpness());
    }

    public static void runUpdater() {

        File file = new File(Bukkit.getWorld("world").getWorldFolder().getAbsolutePath()+"\\playerdata");
        JasperProject plugin = JasperProject.getPlugin();
        File[] files = file.listFiles();

        long startTime = System.nanoTime();
        int total_item_updated = 0;
        for (File playerData : files) {
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
                    plugin.getLogger().info("[ItemPatcher] Detected new Version of "+ getID(item)+" -> "+new_version.getVersion()+". Starting updating item... ");
                    NBTCompound nbt_item = (NBTCompound) item;
                    ItemStack current = NBTItem.convertNBTtoItem(nbt_item);//ReadWriteNBT -> ItemStack
                    ItemStack patched_item = JItem.convertFrom(current).patch(new_version).getItem();
                    item.mergeCompound(NBTItem.convertItemtoNBT(patched_item));
                    total_item_updated++;
                }
                playernbt.save();
                String time2 = String.format("%.2f", (System.nanoTime() - startTime) / 1_000_000.0);
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
        return getBukkitValues(item).getString(JKey.Main.toString());
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
