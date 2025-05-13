package me.jasper.jasperproject.Dungeon;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JMinecraft.Player.PlayerGroup;
import me.jasper.jasperproject.JMinecraft.Player.PlayerManager;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class Dungeon extends DungeonGenerator {
    private final static Map<String, Dungeon> instance = new HashMap<>();
    private final PlayerGroup group;
    @Setter private int deathCount = 0;
    private long start_time;
    private int max_score;
    private int current_score = 0;
    private BukkitTask tick;
    private final Map<UUID, PlayerGrave> deathPlayers = new HashMap<>();
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    public Dungeon(PlayerGroup group){
        this.group = group;
        instance.put(instance_key, this);
    }

    public void enter(){
        generate();
        Point entrance = getHandler().getEntrance();
        Location entrance_location = new Location(Bukkit.getWorld(instance_key), entrance.x * 32, 74, entrance.y * 32);
        Objective objective = scoreboard.registerNewObjective("dungeon", "dummy", Component.text("<bold>Dungeon -FloorN").color(NamedTextColor.GOLD));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        int score = 15;
        objective.getScore(ChatColor.GRAY+"discovered "+(current_score/max_score)*100+" %").setScore(--score);
        objective.getScore(ChatColor.GRAY+"timer: N/A").setScore(--score);
        for (JPlayer member : group.getMembers()) {
            Player bukkitPlayer = member.getBukkitPlayer();
            bukkitPlayer.teleport(entrance_location);
            objective.getScore(bukkitPlayer.getName()+": "+bukkitPlayer.getHealth()).setScore(--score);
        }
    }

    public void start(){
        this.start_time = System.currentTimeMillis();
        for (Room room : getHandler().getRoom()) {
//            dungeon_score+=
        }
        tick = new BukkitRunnable() {
            @Override
            public void run() {
                Objective obj = scoreboard.getObjective("dungeon");
                obj.getScore(ChatColor.GRAY+ Util.timer(System.currentTimeMillis()-start_time)).setScore(14);
            }
        }.runTaskTimer(JasperProject.getPlugin(), 20, 20);
    }

    public void close(){
        Location spawn = Bukkit.getWorld("spawn").getSpawnLocation();
        for (JPlayer member : group.getMembers()) {
            member.getBukkitPlayer().teleport(spawn);
        }
        closeWorld();
        instance.remove(instance_key);
        tick.cancel();
    }

    public void exit(Player player){
        if(deathPlayers.containsKey(player.getUniqueId())) revive(player);
        deathPlayers.put(player.getUniqueId(), null);
        if (deathPlayers.size()>=group.getMembers().size()) close();
        Location spawn = Bukkit.getWorld("spawn").getSpawnLocation();
        player.teleport(spawn);
    }

    public void rejoin(Player player){
        Point entrance = getHandler().getEntrance();
        Location entrance_location = new Location(Bukkit.getWorld(instance_key), entrance.x * 32, 74, entrance.y * 32);
        player.teleport(entrance_location);
        PlayerGrave inv = new PlayerGrave();
        inv.save(player);
        player.getInventory().setItem(0, new ItemStack(Material.BEDROCK));
        deathPlayers.put(player.getUniqueId(), inv);
    }

    public void revive(Player player){
        PlayerGrave content = deathPlayers.remove(player.getUniqueId());
        content.restore(player);
        player.setInvisible(false);
    }

    public static class DungeonListener implements Listener {
        @EventHandler
        public void onPlayerDeath(PlayerDeathEvent e){
            Player player = e.getPlayer();
            String name = player.getWorld().getName();
            if(!Dungeon.instance.containsKey(name)) return;
            Dungeon dungeon = Dungeon.instance.get(name);
            if (dungeon.deathPlayers.size()>=dungeon.group.getMembers().size()) {
                for (Player player1 : Bukkit.getWorld(dungeon.instance_key).getPlayers()) {
                    player1.sendMessage(Component.text("This instance will be closed in 15 seconds!").color(NamedTextColor.RED));
                }
                Bukkit.getScheduler().runTaskLater(JasperProject.getPlugin(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        dungeon.close();
                    }
                }, 300L);
            }
            PlayerGrave inventoryContent = new PlayerGrave();
            inventoryContent.save(player);
            dungeon.getDeathPlayers().put(player.getUniqueId(), inventoryContent);
            dungeon.setDeathCount(dungeon.getDeathCount()+1);
            e.setCancelled(true);
        }

        @EventHandler
        public void onPlayerDisconnect(PlayerQuitEvent e){
            String name = e.getPlayer().getWorld().getName();
            if(Dungeon.instance.containsKey(name)){
                PlayerManager.getJPlayer(e.getPlayer()).setLastInstance(name);
            }
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e){
            JPlayer jPlayer = PlayerManager.getJPlayer(e.getPlayer());
            String instance = jPlayer.getLastInstance();
            if (Dungeon.instance.containsKey(instance)) {
                Dungeon dungeon = Dungeon.instance.get(instance);
                dungeon.rejoin(e.getPlayer());
            }
        }
    }
    public static class PlayerGrave {
        private ItemStack[] inventory;
        private ItemStack[] armor;
        private ItemStack offHand;
        private Location lastDeathLocation;

        public void save(Player player){
            PlayerInventory container = player.getInventory();
            inventory = container.getContents().clone();
            armor = container.getArmorContents().clone();
            offHand = container.getItemInOffHand();
            lastDeathLocation = player.getLocation();
            container.clear();
            player.setInvisible(true);
            player.getInventory().setItem(0, new ItemStack(Material.BEDROCK));
        }

        public void restore(Player player){
            PlayerInventory container = player.getInventory();
            container.setItemInOffHand(offHand);
            container.setContents(inventory);
            container.setArmorContents(armor);
            player.teleport(lastDeathLocation);
            player.setInvisible(false);
        }
    }
}
