package me.jasper.jasperproject.Jam;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class ClockUtility {
    final private JasperProject plugin;
    public ClockUtility(JasperProject plugin) {
        this.plugin = plugin;
    }

    void detakJam(){
        new BukkitRunnable(){
            Location ClockLocation = Clock.get().getLocation("Clock.Location");
            @Override
            public void run() {
                ClockLocation.getWorld().playSound(
                        ClockLocation,
                        Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON,
                        4.8f,
                        2f
                );

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ClockLocation.getWorld().playSound(
                                ClockLocation,
                                Sound.BLOCK_WOODEN_BUTTON_CLICK_ON,
                                4.8f,
                                2f
                        );
                    }
                }.runTaskLater(plugin, 20); // 20 ticks = 1 second delay
            }
        }.runTaskLater(plugin,40);

    }

    void tick_ke_menit(long tickDayWorld){

        int Jam = 6;
        int menit;
        double tick = (double) tickDayWorld;
        double menit_Desimal = (tick/100)*6.0;
        if(tickDayWorld <18000){
            Jam += (int) (menit_Desimal/60);
            menit = (int) menit_Desimal;
            menit-= Jam*60-360;
            
            Clock.get().set("Clock.menit",menit);
            Clock.get().set("Clock.jam",Jam);
            Clock.get().set("Clock.isDay",true);
            Clock.save();
        }
        if(tickDayWorld >= 18000){
            Jam = -18;
            Jam += (int) (menit_Desimal/60);
            menit = ((int) menit_Desimal) - 1080;
            menit-= Jam*60;
            Clock.get().set("Clock.jam",Jam);
            Clock.get().set("Clock.menit",menit);
            Clock.save();

            if(Clock.get().getBoolean("Clock.isDay")){

                Clock.get().set("Clock.day",Clock.get().getInt("Clock.day")+1);
                Clock.get().set("Clock.isDay",false);
                Clock.save();
                new BukkitRunnable(){
                    Location ClockLocation = Clock.get().getLocation("Clock.Location");
                    @Override
                    public void run() {
                        ClockLocation.getWorld().playSound(
                                ClockLocation,
                                Sound.BLOCK_BELL_USE,
                                10f,
                                0.5f
                        );

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ClockLocation.getWorld().playSound(
                                        ClockLocation,
                                        Sound.BLOCK_BELL_USE,
                                        10f,
                                        0.5f
                                );
                            }
                        }.runTaskLater(plugin, 20); // 20 ticks = 1 second delay
                    }
                }.runTaskLater(plugin,40);
                plugin.getServer().broadcastMessage("Selamat pagi! "+Clock.get().getInt("Clock.day") );
            }
        }
    }
    void perhitunganWaktu(){
                long time = Clock.get().getLocation("Clock.Location").getWorld().getTime();
                Clock.get().set("Clock.world_tick",time);
                tick_ke_menit(time);
                
                if(Clock.get().getInt("Clock.day")>30){
                    Clock.get().set("Clock.month",Clock.get().getInt("Clock.month")+1);
                    Clock.get().set("Clock.day",Clock.get().getInt("Clock.day")-30);
                    Clock.save();
                    plugin.getServer().broadcastMessage(ChatColor.GREEN +"Bulan Baru! "+Clock.get().getInt("Clock.month"));
                }
                if(Clock.get().getInt("Clock.month")>12){
                    Clock.get().set("Clock.year",Clock.get().getInt("Clock.year")+1);
                    Clock.get().set("Clock.day",1);
                    Clock.get().set("Clock.month",Clock.get().getInt("Clock.month")-12);
                    Clock.save();
                    plugin.getServer().broadcastMessage(ChatColor.BLUE+"Happy new year! "+Clock.get().getInt("Clock.year"));
                }
    }

    HashMap<Integer,Entity> ModelFinder(Location ClockLocation, String tag){
        HashMap<Integer,Entity> model = new HashMap<>();
        int design_nearby = 1;
        for (Entity entity : ClockLocation.getWorld().getNearbyEntities(ClockLocation,10,10,10)){
            if(entity.getScoreboardTags().contains(tag)){
                model.put(design_nearby,entity);
                design_nearby++;
                plugin.getServer().broadcastMessage(ChatColor.GREEN+"Entity with tag "+tag+" found!");
            }
            if(entity.getScoreboardTags().contains(tag) && entity.getType() == EntityType.ARMOR_STAND){
                entity.remove();
            }
        }
        return model;
    }

    void jarumMenit(){
        Location ClockLocation = Clock.get().getLocation("Clock.Location");
        float pitch = 0, yaw = 0, Menit = 0;
        try {
            pitch = ClockLocation.getPitch() - 90;
            yaw = ClockLocation.getYaw() - 90;
            Menit =  (float) Clock.get().getDouble("Clock.menit");
        }catch (NullPointerException ignored){}

        float putaran_menit =  6*Menit;

        UUID as = UUID.fromString(Objects.requireNonNull(Clock.get().getString("Clock.jarum.menit.UUID_armorstand")));
        UUID m = UUID.fromString(Objects.requireNonNull(Clock.get().getString("Clock.jarum.menit.UUID_model")));

        Entity armorstand =  Bukkit.getEntity(as);
        Entity model =  Bukkit.getEntity(m);


        try {
            Location posArmorStand = ClockLocation.clone();
            posArmorStand.setYaw(yaw);
            posArmorStand.setPitch(pitch + putaran_menit);
            armorstand.teleport(posArmorStand);

            Location armorstandLocationv2 = armorstand.getLocation();
            Vector directionv2 = armorstandLocationv2.getDirection();
            Location newLocationv2 = armorstandLocationv2.add(directionv2.multiply(0.825));
            model.teleport(newLocationv2);
        }catch (NullPointerException ignored){
        }

    }
    void jarumJam(){
        Location ClockLocation = Clock.get().getLocation("Clock.Location");
        float pitch = ClockLocation.getPitch() - 90;
        float yaw = ClockLocation.getYaw() - 90;

        double Jam =  (Clock.get().getDouble("Clock.jam") + (Clock.get().getDouble("Clock.menit")/60));
        double putaran_jam =  30*Jam;


        UUID as = UUID.fromString(Objects.requireNonNull(Clock.get().getString("Clock.jarum.jam.UUID_armorstand")));
        UUID m = UUID.fromString(Objects.requireNonNull(Clock.get().getString("Clock.jarum.jam.UUID_model")));
        Entity armorstand =  Bukkit.getEntity(as);
        Entity  model =  Bukkit.getEntity(m);

        try{
            Location posArmorStand = ClockLocation.clone();
            posArmorStand.setYaw(yaw);
            posArmorStand.setPitch((float) (pitch + putaran_jam));
            armorstand.teleport(posArmorStand);

            Location armorstandLocation = armorstand.getLocation();
            Vector direction = armorstandLocation.getDirection();
            Location newLocation = armorstandLocation.add(direction.multiply(0.825));
            model.teleport(newLocation);
        }catch (NullPointerException ignored){}
    };
}