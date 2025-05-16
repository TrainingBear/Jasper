package me.jasper.jasperproject.Dungeon;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JMinecraft.Player.PlayerGroup;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.TookTimer;
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
import org.bukkit.scoreboard.*;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class Dungeon extends DungeonGenerator {
    public final static Map<String, Dungeon> instance = new HashMap<>();
    private final PlayerGroup group;
    @Setter private int deathCount = 0;
    private long start_time;
    private int max_score;
    private int current_score = 0;
    private BukkitTask tick;
    private final Map<UUID, PlayerGrave> deathPlayers = new HashMap<>();
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private final Objective objective = scoreboard.registerNewObjective("dungeon", Criteria.DUMMY, Component.text("<b>Dungeon -FloorN").color(NamedTextColor.GOLD));
    public Dungeon(PlayerGroup group, int p, int l){
        super(p, l);
        this.group = group;
        instance.put(instance_key, this);
    }
    public void enter(){
        TookTimer.run("generate dungeon", () -> generate());
        Point entrance = getHandler().getEntrance();
        Location entrance_location = new Location(Bukkit.getWorld(instance_key), entrance.x * 32, 74, entrance.y * 32);
        int score = 15;

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(ChatColor.GRAY.toString()).setScore(--score);
        scoreboard.registerNewTeam("discover").addEntry(ChatColor.GRAY.toString());
        objective.getScore(String.valueOf(ChatColor.RESET)).setScore(--score);
        scoreboard.registerNewTeam("timer").addEntry(ChatColor.RESET.toString());;
        for (JPlayer member : group.getMembers()) {
            DungeonMap.sendMap(member.getBukkitPlayer());
            Bukkit.broadcast(member.getBukkitPlayer().displayName().append(Component.text("enter FLOOR_N!").color(NamedTextColor.GREEN)));
            Player bukkitPlayer = member.getBukkitPlayer();
            getMap().renderCanvas(bukkitPlayer);
            bukkitPlayer.teleport(entrance_location);
            objective.getScore("§"+--score).setScore(score);
            scoreboard.registerNewTeam(bukkitPlayer.getName()).addEntry("§"+--score);
            member.setLastInstance(instance_key);
            bukkitPlayer.setScoreboard(scoreboard);
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
                List<Player> players = Bukkit.getWorld(instance_key).getPlayers();
                if(players.isEmpty()) close();
                getMap().renderCursor(players);
                Team team = scoreboard.getTeam("timer");
                team.prefix(Component.text("time elapsed ").color(NamedTextColor.GREEN).append(Component.text(Util.timer(System.currentTimeMillis()-start_time)).color(NamedTextColor.GRAY)));
                team = scoreboard.getTeam("discover");
                team.prefix(Component.text("discovered "+(current_score/100)*100+"%"));
                for (Player player : players) {
                    team = scoreboard.getTeam(player.getName());
                    team.prefix(player.displayName().append(Component.text(player.getHealth()+" ❤ ").color(NamedTextColor.RED)));
                }
                Bukkit.broadcast(Component.text(instance_key+" is ticking..").color(NamedTextColor.GRAY));
            }
        }.runTaskTimer(JasperProject.getPlugin(), 20, 20);
    }

    public void close(){
        Location spawn = Bukkit.getWorld("spawn").getSpawnLocation();
        for (Player member : Bukkit.getWorld(instance_key).getPlayers()) {
            member.teleport(spawn);
        }
        Bukkit.broadcast(Component.text("closed "+instance_key).color(NamedTextColor.RED));
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
                JPlayer.getJPlayer(e.getPlayer()).setLastInstance(name);
            }
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e){
            JPlayer jPlayer = JPlayer.getJPlayer(e.getPlayer());
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
