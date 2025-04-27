package me.jasper.jasperproject.JasperItem.ItemAttributes.Abilities;

import lombok.Getter;
import lombok.val;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Util.ItemUtils;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.JasperItem.Util.TRIGGER;
import me.jasper.jasperproject.Util.SignGUI;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Warper extends ItemAbility{
    private static Warper instance;
    public static Warper getInstance(){
        if(instance == null) instance = new Warper();
        return instance;
    }

    @Getter private Action ActionPlayer;
    HashMap<UUID, int[]> target = new HashMap<>();

    public Warper(){
    }
    public Warper(float cooldown, int WarpRange){
        this.setRange(WarpRange);
        this.setCooldown(cooldown);
        addLore(List.of(
                MiniMessage.miniMessage().deserialize("<!i><gold>Ability: <color:#a900d1><b>Warper <yellow>(RIGHT CLICK & LEFT CLICK)")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Warping you to checkpoint coordinate")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>that you inputed <color:#95945B>(Sneak + Left Click)")
                ,MiniMessage.miniMessage().deserialize("<!i><gray>Max warp range: "+WarpRange+" blocks")
        ));

    }
    public Warper(int WarpRange, float cooldown, PlayerInteractEvent e){
        setRange(WarpRange);
        setCooldown(cooldown);
        setPlayer(e.getPlayer());
        this.ActionPlayer = e.getAction();
    }

    @EventHandler
    public void WarperListener(Warper e){
        if((e.getActionPlayer().equals(Action.LEFT_CLICK_AIR)||e.getActionPlayer().equals(Action.LEFT_CLICK_BLOCK))&&e.getPlayer().isSneaking()){
//            e.getPlayer().sendBlockChange(e.getPlayer().getEyeLocation().getBlock().getLocation(), e.getPlayer().getEyeLocation().getBlock().getBlockData());
            e.setCancelled(true);
            String[] builtInText= {
                    ""
                    ,"^^^^^^^^^^^^"
                    ,"Input coordinates"
                    ,"x y z / x, y, z"
            };
            SignGUI.getInstance().open(
                    e.getPlayer(), builtInText, Material.WARPED_SIGN, (p, lines, signLoc) -> {
                        p.sendBlockChange(signLoc, signLoc.getBlock().getBlockData());//turn back to normal

                        String[] coordinate = lines[0].trim().split("\\s*,\\s*|\\s+");

                        if(coordinate.length==1&&coordinate[0].isEmpty()) return;
                        if(coordinate.length!=3){
                            p.sendMessage(MiniMessage.miniMessage().deserialize("<red><b>ABILITY</b> Please input 3 argument of coordinate</red>"));
                            ItemUtils.playPSound(p,Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 0f);
                            return;
                        }
                        int[] playerLOC = {p.getLocation().getBlockX(),p.getLocation().getBlockY(),p.getLocation().getBlockZ()};
                        int[] savedCordinate= new int[3];

                        for (byte i =0; i < coordinate.length ; i++) {
                            coordinate[i] = coordinate[i].trim();
                            if (coordinate[i].contains("~")) {
                                if(coordinate[i].charAt(0) != '~') {
                                    p.sendMessage(MiniMessage.miniMessage().deserialize("<red><b>ABILITY</b> Coordinate is not valid</red>"));
                                    ItemUtils.playPSound(p,Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 0f);
                                    return;
                                }
                                else if(coordinate[i].equals("~")) coordinate[i] = String.valueOf(playerLOC[i]);
                                else coordinate[i] = String.valueOf(playerLOC[i] + Integer.parseInt(coordinate[i].substring(1)));
                            }
                            try{
                                savedCordinate[i] = Integer.parseInt(coordinate[i]);
                            } catch (NumberFormatException exception) {
                                p.sendMessage(MiniMessage.miniMessage().deserialize("<red><b>ABILITY</b> Coordinate is not valid</red>"));
                                ItemUtils.playPSound(p,Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 0f);
                                return;
                            }
                        }
                        p.sendMessage(MiniMessage.miniMessage().deserialize("<color:#b100db><b>ABILITY</b> Set coordinate to: </color><color:#dd00ed><click:\"copy_to_clipboard\":"
                                        +String.join(", ",coordinate) +">"+ String.join(", ",coordinate)+"</click></color>"));
                        ItemUtils.playPSound(p,Sound.BLOCK_BEACON_POWER_SELECT, 1, 1.65f);

                        target.put(e.getPlayer().getUniqueId(), savedCordinate.clone());
//                                Placeholder.unparsed("x", coordinate[0]),
//                                Placeholder.unparsed("y", coordinate[1]),
//                                Placeholder.unparsed("z", coordinate[2]),
//                                Placeholder.unparsed("value", coordinate.toString()))
                    });
        }
        else if((e.getActionPlayer().equals(Action.RIGHT_CLICK_BLOCK)||e.getActionPlayer().equals(Action.RIGHT_CLICK_AIR))&& e.getPlayer().isSneaking()){
            if(!target.containsKey(e.getPlayer().getUniqueId())){
                e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red><b>ABILITY</b> Please input coordinate (Left click + Sneak)</red>"));
                ItemUtils.playPSound(e.getPlayer(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE,1,0.5f);
                return;
            }
            
            Player player = e.getPlayer();
            UUID ID = player.getUniqueId();
            Location targetToTP = new Location(player.getWorld(), target.get(ID)[0]+.5f,target.get(ID)[1]+.01f,target.get(ID)[2]+.5f)
                    .setDirection(player.getLocation().getDirection());
            float distanceTP = (float) targetToTP.distance(player.getLocation());

            int range = e.getRange();
            if (distanceTP < e.getRange()+.1f) applyCooldown(e,true);
            else{
//                if(hasCooldown(e)) return;
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red><b>TOO FAR! </b>You're <color:#fa3b2d>" + (distanceTP > 1000
                        ? Util.round((distanceTP-range) / 1000f, 1)+"k" : Util.round((distanceTP-range), 1)) + "</color> blocks away from limit!</red>"));
                ItemUtils.playPSound(e.getPlayer(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 0.5f);
                return;
            }

            if(e.isCancelled()) return;

            float trail = (float) Math.min(distanceTP, 6.0); // parameter ke 2 bisa di ubah jarak maxnya
            int steps = (int) Math.max(trail, 1);
            float factor = (float) (1.0 /distanceTP);

            for (byte i = 0; i <= steps; i++) {
                float prog = ((float) i / steps) * trail;
                player.getWorld().spawnParticle(
                        Particle.SOUL_FIRE_FLAME, player.getLocation().add(
                                (targetToTP.getX() - player.getLocation().getX()) * factor * prog,
                                ((targetToTP.getY() - player.getLocation().getY()) * factor * prog)+1,
                                (targetToTP.getZ() - player.getLocation().getZ()) * factor * prog)
                        ,5, 0.2, 0.2, 0.2,
                        0,null,false);
            }

            player.getWorld().spawnParticle(
                    Particle.DUST, player.getLocation().add(0,1,0), 60
                    ,.3f,.4f,.3f,0
                    ,new Particle.DustOptions(Color.fromRGB(214,0,230),1.425f), false);
            player.getWorld().spawnParticle(
                    Particle.DUST, player.getLocation().add(0,1,0), 60
                    ,.3f,.4f,.3f,0
                    ,new Particle.DustOptions(Color.fromRGB(60,60,60),2f), false);

            Util.teleportPlayer(player,targetToTP,false);//TELEPOOOOOOOOOOOOOOOOORTTTTT================   <---   biar jelas codeny ad dstu
            e.setCancelled(true);

            player.getWorld().spawnParticle(
                    Particle.WITCH,player.getLocation().add(0,.5f,0), 100
                    , .3f, .3f, .3f, 0 , null, false);
            player.getWorld().spawnParticle(
                    Particle.TOTEM_OF_UNDYING ,player.getLocation().add(0,1.5f,0), 20
                    , .15f, .15f, .15f, 0.2f , null, false);
            ItemUtils.playPSound(player, Sound.ENTITY_ENDER_DRAGON_HURT,1,(float) Math.min(1.7, 0.5f + (distanceTP * 0.025f)));
            ItemUtils.playPSound(e.getPlayer(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED,0.5f
                    ,1.6f);
        }
    }

    @EventHandler
    public void trigger(PlayerInteractEvent e){
        val item = e.getPlayer().getInventory().getItemInMainHand();
        if (!ItemUtils.hasAbility(item, this.getKey()))  return;

        if(TRIGGER.Interact.SHIFT_RIGHT_CLICK(e) || TRIGGER.Interact.SHIFT_LEFT_CLICK(e)){

            PersistentDataContainer itemData = ItemUtils.getAbilityComp(item, this.getKey());
            Bukkit.getPluginManager().callEvent(new Warper(
                    itemData.get(JKey.key_range, PersistentDataType.INTEGER),
                    itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                    e
            ));
            e.setCancelled(true);
        }

    }
}
