package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.Util.Structure;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Animator extends ItemAbility implements Listener {
    private static HashMap<UUID, BlockVector3> firstPos = new HashMap<>();
    private static HashMap<UUID, BlockVector3> secondPost = new HashMap<>();
    @Getter private static HashMap<UUID, Region> regions = new HashMap<>();

    public Animator(){
        addLore(List.of(
                ChatColor.translateAlternateColorCodes('&',"&6Ability: &c&lCreator &r&e(RIGHT CLICK & LEFT CLICK)"),
                ChatColor.translateAlternateColorCodes('&',"&7Create a &aAnimation&7 with your"),
                ChatColor.translateAlternateColorCodes('&',"&7Imagination! Create endless of"),
                ChatColor.translateAlternateColorCodes('&',"&7Creativity.")
        ));
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent e){
        if(!ItemUtils.hasAbility(e.getItem(),this.getKey())) return;
        org.bukkit.entity.Player player = e.getPlayer();

        if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            Block block = e.getClickedBlock();
            BlockVector3 pos = BlockVector3.at(block.getX(), block.getY(), block.getZ());
            firstPos.put(player.getUniqueId(), pos);
            player.sendMessage(ChatColor.YELLOW+"You selected first pos!");
            e.setCancelled(true);
        }if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Block block = e.getClickedBlock();
            BlockVector3 pos = BlockVector3.at(block.getX(), block.getY(), block.getZ());
            secondPost.put(player.getUniqueId(), pos);
            player.sendMessage(ChatColor.YELLOW+"You selected second pos!");
            e.setCancelled(true);
        }
        if(!firstPos.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW+"Right click to select post2!");
            return;
        }if(!secondPost.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW+"Left click to select post1!");
            return;
        }
        Region region = new CuboidRegion(BukkitAdapter.adapt(player.getWorld()), firstPos.get(player.getUniqueId()), secondPost.get(player.getUniqueId()));
        regions.put(player.getUniqueId(), region);
        Structure.createBox(player);
    }
}
