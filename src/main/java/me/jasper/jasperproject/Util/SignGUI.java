package me.jasper.jasperproject.Util;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.jasper.jasperproject.JasperProject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.jetbrains.annotations.NotNull;

public final class SignGUI implements Listener {

    private static SignGUI instance;

    public static SignGUI getInstance() {
        if(instance==null){
            instance = new SignGUI();
        }
        return instance;
    }

    private static ProtocolManager protocolManager;
    private static PacketAdapter packetListener;
    private static Map<String, SignGUIListener> listeners;
    private static Map<String, Vector> signLocations;
    private static Location signLoc;
    private static BlockData previousBlockData;

    SignGUI() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        packetListener = new PacketListener(JasperProject.getPlugin());
        protocolManager.addPacketListener(packetListener);
        listeners = new ConcurrentHashMap<>();
        signLocations = new ConcurrentHashMap<>();
    }

    public void open(Player player, @NotNull String[] inputText,Material material, SignGUIListener response) {
        if((player.getLocation().add(0,-2,0).getBlockY() <= 320 && player.getLocation().add(0,-2,0).getBlockY() >= -64)
            && player.getLocation().add(0,-1,0).getBlock().getType().isSolid()) {

            if((player.getLocation().add(0,-3,0).getBlockY() <= 320
                    && player.getLocation().add(0,-3,0).getBlockY() >= -64)) signLoc = player.getLocation().add(0,-3,0);

            else signLoc = player.getLocation().add(0,-2,0);
        }
        else{
            Location behindPlayer = player.getLocation().subtract(player.getLocation().getDirection().normalize().multiply(4))
                    .getBlock().getLocation();

            if(behindPlayer.getBlockY() >= 320 || behindPlayer.getBlockY() <= -64) behindPlayer.setY(player.getLocation().getY());
            if(behindPlayer.getBlockY() >= 320 || behindPlayer.getBlockY() <= -64) signLoc = null;
            else signLoc = behindPlayer;
        }

        if(signLoc !=null) {
            previousBlockData = signLoc.getBlock().getBlockData();
            int x = signLoc.getBlockX(), y = signLoc.getBlockY(), z = signLoc.getBlockZ();

            player.sendBlockChange(signLoc, material.createBlockData());
            signLocations.put(player.getName(), new Vector(x, y, z));
            Block placeholderBlock = signLoc.getBlock();
            placeholderBlock.setType(material);
            Sign signBlock = (Sign) placeholderBlock.getState();
            SignSide frontSide = signBlock.getSide(Side.FRONT);
            for(byte i = 0 ; i < inputText.length ; i++) frontSide
                    .line(i, MiniMessage.miniMessage().deserialize(inputText[i]));

            player.sendBlockUpdate(signLoc,signBlock);

            final PacketContainer packetOPENSIGNEDITOR = protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
            packetOPENSIGNEDITOR.getBlockPositionModifier().write(0,
                    new com.comphenix.protocol.wrappers.BlockPosition(x, y, z));
            packetOPENSIGNEDITOR.getBooleans().write(0, true);


            //            protocolManager.sendServerPacket(player, packetSIGNTEXT);
            try {
                protocolManager.sendServerPacket(player, packetOPENSIGNEDITOR);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            listeners.put(player.getName(), response);
        }
    }

    public void destroy() {
        protocolManager.removePacketListener(packetListener);
        listeners.clear();
        signLocations.clear();
    }

    public interface SignGUIListener {
        void onSignDone(Player player, String[] lines, Location signLoc, BlockData previousBlockLoc);
    }

    static class PacketListener extends PacketAdapter {

        Plugin plugin;
        public PacketListener(Plugin plugin) {
            super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN);
            this.plugin = plugin;
        }

        @Override
        public void onPacketReceiving(PacketEvent event) {
            Vector v = signLocations.remove(event.getPlayer().getName());
            if (v == null) return;
            List<BlockPosition> event_sign_location = event.getPacket().getBlockPositionModifier().getValues();
            if (event_sign_location.get(0).getX() != v.getBlockX()) return;
            if (event_sign_location.get(0).getY() != v.getBlockY()) return;
            if (event_sign_location.get(0).getZ() != v.getBlockZ()) return;

            final String[] lines = event.getPacket().getStringArrays().getValues().get(0);
            final SignGUIListener response = listeners.remove(event.getPlayer().getName());
            if (response != null) {
                event.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        response.onSignDone(event.getPlayer(), lines, signLoc,previousBlockData));
            }
        }

    }

}
