package me.jasper.jasperproject.JasperItem.Util;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTFile;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import lombok.val;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemStats;
import me.jasper.jasperproject.JasperItem.Jitem;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ItemPatcher {

    public static void runJitemUpdater() throws IOException, IllegalAccessException {

        File file = new File(Bukkit.getWorld("world").getWorldFolder().getAbsolutePath()+"\\playerdata");
        JasperProject plugin = JasperProject.getPlugin();
        File[] files = file.listFiles();

        long startTime = System.nanoTime();
        int total_item_updated = 0;
        for (File playerData : files) {
            int index = playerData.getName().indexOf('.');
            if(!playerData.getName().substring(index).equals(".dat")) continue;

            UUID id = UUID.fromString(playerData.getName().substring(0, index));
            Player player = Bukkit.getPlayer(id);
            if(player!=null){
                player.sendMessage("You are not null");
                player.kickPlayer("Updating Your Items... ");
                player.saveData();
            }


            NBTFile playernbt = new NBTFile(playerData);
            NBTCompoundList inventory = playernbt.getCompoundList("Inventory");
            for (ReadWriteNBT item : inventory) {
                if(!hasCustomID(item)) continue;
                val items = ItemManager.getInstance().getItems();
                if(!items.containsKey(getID(item))) continue;
                Jitem newVer = ItemManager.getInstance().getItems().get(getID(item));
                if(getVersion(item) == newVer.getVersion()) continue;

                plugin.getLogger().info("Detected new Version of "+ getID(item)+" -> "+newVer.getVersion()+". Starting updating item... ");
                NBTCompound updateItem = (NBTCompound) item;

                //ReadWriteNBT -> ItemStack
                ItemStack lastVer = NBTItem.convertNBTtoItem(updateItem);
                lastVer = Patch(lastVer, newVer);
                item.mergeCompound(NBTItem.convertItemtoNBT(lastVer));
                total_item_updated++;
            }
            playernbt.save();
            String time2 = String.format("%.2f", (System.nanoTime() - startTime) / 1_000_000.0);
            plugin.getLogger().info("Updating "+playernbt.getCompound("bukkit").getString("lastKnownName")+" inventory took "+time2+"ms");
        }
        String time2 = String.format("%.2f", (System.nanoTime() - startTime) / 1_000_000.0);
        plugin.getLogger().info(total_item_updated+" Items have been updated! ("+time2+"ms)");
    }

    private static long getVersion(ReadWriteNBT item){
        return getBukkitValues(item).getLong(JKey.Version.toString());
    }
    private static String getID(ReadWriteNBT item){
        return getBukkitValues(item).getString(JKey.Main.toString());
    }

    private static void updateBaseStats(ItemStack lastVer, Jitem newVer) throws IllegalAccessException {
        ItemStats newStats = new ItemStats();
        newStats.getStatsFromItem(lastVer);
        newStats.setBaseStats(newVer.getStats());
    }
    private static ItemStack Patch(ItemStack lastVer, Jitem newVer) throws IllegalAccessException {
        Jitem LastVersion = Jitem.convertFrom(lastVer, newVer.getCustom_lore());

//        Set Base item stats
        ItemStats newStats = new ItemStats();
        newStats.getStatsFromItem(lastVer);
        newStats.setBaseStats(newVer.getStats());
        newStats.setModifiers(newVer.getStats());
        newStats.calculateFinalStats();
        LastVersion.replaceStats(newStats);

        //update the lore
        LastVersion.getCustom_lore().clear();
        LastVersion.getCustom_lore().addAll(newVer.getCustom_lore());

        //update item abilities
        LastVersion.getAbilities().clear();
        LastVersion.getAbilities().addAll(newVer.getAbilities());

        //update base item rarity
        LastVersion.setBaseRarity(newVer.getBaseRarity());

        //update base item Name
        LastVersion.setDefaultItem_name(newVer.getDefaultItem_name());

        //update item ability
        LastVersion.getAbilities().clear();
        LastVersion.getAbilities().addAll(newVer.getAbilities());


        LastVersion.update();
        return LastVersion.getItem();
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
