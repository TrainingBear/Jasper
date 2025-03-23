package me.jasper.jasperproject.Command;

import me.jasper.jasperproject.FileConfiguration.LaunchPadConfiguration;
import me.jasper.jasperproject.JasperProject;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

//import static org.bukkit.Bukkit.createBlockData;
import static org.bukkit.Bukkit.getServer;
import java.util.HashMap;
import java.util.UUID;

public class LaunchPad implements CommandExecutor, Listener {
    private final JasperProject plugin;
    private Pig launcher;

    public LaunchPad(JasperProject plugin){
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player p)) {
            commandSender.sendMessage("This command sould be executed by player");
            return false;
        }


        if(strings.length>=2) {
            if (strings[0].equalsIgnoreCase("create") ) {
                    LaunchPadConfiguration.get().addDefault("Launch_Pad", strings[1]);
//                    LaunchPadConfiguration.get().set("Launch_Pad", p.getName()+" Create "+strings[1]);
                    LaunchPadConfiguration.save();
                    p.sendMessage("created Launch pad named "+strings[1]);
                    return true;
            }
            if (strings[0].equalsIgnoreCase("edit") ) {
                    Location pos1;
                    Location pos2;
                        strings[1] = strings[1]+".";
                    if (strings[2].equalsIgnoreCase("pos1")) {
                        pos1 = p.getLocation();
                        LaunchPadConfiguration.get().set("Launch_Pad." + strings[1]+"pos1", pos1);
                        LaunchPadConfiguration.save();
                        p.sendMessage("you created pos1!");
                        return true;
                    }
                    if (strings[2].equalsIgnoreCase("pos2")) {
                        pos2 = p.getLocation();
                        LaunchPadConfiguration.get().set("Launch_Pad." + strings[1]+"pos2", pos2);
                        LaunchPadConfiguration.save();
                        p.sendMessage("you created pos2!");
                        return true;
                    }
                    if (strings[2].equalsIgnoreCase("check")) {
                        int startX = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos1").getX();
                        int startY = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos1").getY();
                        int startZ = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos1").getZ();
                        int endX   = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos2").getX();
                        int endY   = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos2").getY();
                        int endZ   = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos2").getZ();
                        p.sendMessage("start : " + startX + ", " + startY + ", " + startZ + ", " + "    end : " + endX + ", " + endY + ", " + endZ);
                        p.sendMessage(ChatColor.GOLD + "Perpindahan = " + (endX - startX) + " , " + (endY - startY) + " , " + (endZ - startZ) + "! ");
                        double X = endX - startX;
                        double Y = endY - startY;
                        double Z = endZ - startZ;

                        double harizontal = Math.sqrt((X * X) + (Z * Z));
                        double jarak = Math.sqrt((harizontal * harizontal) + (Y * Y));
                        p.sendMessage(ChatColor.GOLD + "Jarak dari titik start -> end : " + jarak);
                    }
            }
            if(strings[0].equalsIgnoreCase("run")) {
                UUID playerUUID = p.getUniqueId();
                long currentTime = System.currentTimeMillis() / 1000;
                final HashMap<UUID, Long> cooldowns = new HashMap<>();
                if(cooldowns.containsKey(playerUUID)){
                    long LastUsed = cooldowns.get(playerUUID);
                    if ((LastUsed + 2) > currentTime) {
                        long timeLeft = (LastUsed + 2) - currentTime;
                        p.sendMessage("You must wait " + timeLeft + " seconds before using this command again.");
                        return true;
                    }
                }
                cooldowns.put(playerUUID, currentTime);
                p.sendMessage("currentime "+currentTime);
                p.sendMessage("playeruuid "+playerUUID);

                int startX = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos1").getX();
                int startY = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos1").getY();
                int startZ = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos1").getZ();
                int endX   = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos2").getX();
                int endY   = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos2").getY();
                int endZ   = (int) LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos2").getZ();
                Location start = LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos1");
                Location   end = LaunchPadConfiguration.get().getLocation("Launch_Pad." + strings[1]+".pos2");
                double X = endX - startX;
                double Y = endY - startY;
                double Z = endZ - startZ;
                double horizontal = Math.sqrt((X * X) + (Z * Z));
                final double R = Math.sqrt((horizontal * horizontal) + (Y * Y));


                Vector direction = end.toVector().subtract(start.toVector()).normalize();

                launcher = (Pig) getServer().getWorld("spawn").spawnEntity(start, EntityType.PIG);
                launcher.addScoreboardTag("launcher");
                launcher.setInvulnerable(true);
                launcher.setSaddle(true);
                launcher.addPassenger(p);
                launcher.setSilent(true);


                p.playSound(start, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 10, 1);
                p.spawnParticle(Particle.EXPLOSION, start, 10, 0, 0, 0, 2);

                p.setInvulnerable(true);

                new BukkitRunnable() {
                    final double angle = 45;
                    final double gravity = 9.8;
                    final double angleInRadians = Math.toRadians(angle);

                    final double vx = Math.sqrt((R * gravity) / (Math.sin(2 * angle)));
                    final double vy = (Math.sqrt(2 * gravity * (R / 2.25)) / Math.sin(angle));
                    final double v = (vy / Math.sin(angle));


                    final double velocityX = vx * Math.cos(angleInRadians);
                    final double velocityY = vy * Math.sin(angleInRadians);
                    final double[] tc = {0};
                    double dt = 0.1; // Time increment
                    @Override
                    public void run() {
                        double x = velocityX * tc[0];
                        double y = velocityY * tc[0] - (0.5 * gravity * tc[0] * tc[0]);

                        Location updatedLocation = start.clone().add(direction.clone().multiply(x)).add(0, y, 0);

                        Vector velocity = updatedLocation.toVector().subtract(launcher.getLocation().toVector());
                        launcher.setVelocity(velocity.multiply(1));


                        p.getWorld().spawnParticle(Particle.CLOUD, launcher.getLocation(), 2, 0, 0, 0, 0.2);
                        tc[0] += dt;
                        p.sendMessage("a "+tc[0] );
                        if (tc[0] > (2.3 * velocityY / gravity)) {
                            this.cancel();
                            for (Entity entity : getServer().getWorld("spawn").getEntities()){
                                if (entity.getScoreboardTags().contains("launcher")){
                                    ((LivingEntity) entity).setHealth(0);
                                }
                            }
                            p.setInvulnerable(false);
                            p.teleport(launcher.getLocation());
                            p.spawnParticle(Particle.EXPLOSION, launcher.getLocation(), 1, 0, 0, 0, 2);
                            launcher.setHealth(0);
                            p.sendMessage("Here some list of launch_pad: "+LaunchPadConfiguration.get().getString("Launch_Pad"));
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
            return true;
        }
        if(strings.length==1){
            if(strings[0].equalsIgnoreCase("list")){
                p.sendMessage(LaunchPadConfiguration.get().getString("Launch_Pad"));
            }
        }
        return true;
    }


        @EventHandler
        public void onDismount(EntityDismountEvent e){

            Entity entity = e.getDismounted();
            if(entity.getScoreboardTags().contains("launcher")){
                e.setCancelled(true);
            }
        }
        @EventHandler
        public void onDeath(EntityDeathEvent e){
            Entity entity = e.getEntity();
            if(entity.getScoreboardTags().contains("launcher")){
                e.getDrops().clear();
            }
        }

}
