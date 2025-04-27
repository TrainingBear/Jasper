package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.TRIGGER;
import me.jasper.jasperproject.Util.CustomStructure.Structure;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Animator extends ItemAbility {
    private static Animator instance;
    public static Animator getInstance(){
        if(instance == null) instance = new Animator();
        return instance;
    }

    private static final HashMap<UUID, BlockVector3> firstPos = new HashMap<>();
    private static final HashMap<UUID, BlockVector3> secondPost = new HashMap<>();
    @Getter private static HashMap<UUID, Region> regions = new HashMap<>();

    public Animator(){

    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent e){
        if(!Util.hasAbility(e.getItem(),this.getKey())) return;
        org.bukkit.entity.Player player = e.getPlayer();

        if(TRIGGER.Interact.LEFT_CLICK_BLOCK(e)){
            Block block = e.getClickedBlock();
            BlockVector3 pos = BlockVector3.at(block.getX(), block.getY(), block.getZ());
            firstPos.put(player.getUniqueId(), pos);
            player.sendMessage(NamedTextColor.YELLOW+"You selected first pos!");
            e.setCancelled(true);
        }if(TRIGGER.Interact.RIGHT_CLICK_BLOCK(e)){
            Block block = e.getClickedBlock();
            BlockVector3 pos = BlockVector3.at(block.getX(), block.getY(), block.getZ());
            secondPost.put(player.getUniqueId(), pos);
            player.sendMessage(NamedTextColor.YELLOW+"You selected second pos!");
            e.setCancelled(true);
        }
        if(!firstPos.containsKey(player.getUniqueId())) {
            player.sendMessage(NamedTextColor.YELLOW+"Right click to select post2!");
            return;
        }if(!secondPost.containsKey(player.getUniqueId())) {
            player.sendMessage(NamedTextColor.YELLOW+"Left click to select post1!");
            return;
        }
        Region region = new CuboidRegion(BukkitAdapter.adapt(player.getWorld()), firstPos.get(player.getUniqueId()), secondPost.get(player.getUniqueId()));
        regions.put(player.getUniqueId(), region);
        Structure.createBox(player);
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                MiniMessage.miniMessage().deserialize("<!i><gold>Ability: <red><b>Creator <yellow>(RIGHT CLICK & LEFT CLICK)")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Create a <green>Animation</green> with your")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Imagination! Create endless of")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Creativity")
        );
    }
}
