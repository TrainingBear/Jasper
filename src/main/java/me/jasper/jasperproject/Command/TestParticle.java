package me.jasper.jasperproject.Command;

import me.jasper.jasperproject.JasperProject;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TestParticle implements CommandExecutor {
    JasperProject plugin;
    public TestParticle(JasperProject plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player p)){
            commandSender.sendMessage("You not allowed to send this command!");
            return true;
        }

        int radius = 1;
        final double pi = 3.14;
        final double[] index = {0};
        final double[] y = {-1.5};
        final int[] c = {0};

        World world = p.getWorld();
        world.playSound(p.getEyeLocation().add(0,0,0), Sound.BLOCK_PORTAL_TRIGGER,1f,1.9f);
        world.spawnParticle(Particle.PORTAL,p.getEyeLocation().add(0, -1.65,0),30,0,0,0,3);
        new BukkitRunnable(){
            @Override
            public void run(){
                double x = Math.sin((2*pi* index[0])/20);
                double z = Math.cos((2*pi* index[0])/20);
//        p.spawnParticle(Particle.DUST_COLOR_TRANSITION,p.getEyeLocation().add(x, y[0],z),1, new Particle.DustTransition(Color.fromRGB(0,0,0),Color.fromRGB(1,1,1),2.5f));
//        p.spawnParticle(Particle.DUST_COLOR_TRANSITION,p.getEyeLocation().add(-(x), y[0],-(z)),1, new Particle.DustTransition(Color.fromRGB(0,0,0),Color.fromRGB(1,1,1),2.5f));
                world.spawnParticle(Particle.DUST,p.getEyeLocation().add(x, y[0],z),1,new Particle.DustOptions(Color.fromRGB(c[0],c[0],c[0]),2.5f));
                world.spawnParticle(Particle.DUST,p.getEyeLocation().add(-(x), y[0],-(z)),1,new Particle.DustOptions(Color.fromRGB(c[0],c[0],c[0]),2.5f));
                c[0] = c[0]+23;
                y[0] = y[0] +0.2;
                index[0]++;
                if(index[0]==10){
                    world.spawnParticle(Particle.EXPLOSION,p.getEyeLocation().add(0, -1.65,0),3,0,0.1,0,1.5);
                    world.spawnParticle(Particle.TOTEM_OF_UNDYING,p.getEyeLocation().add(0, -1.75,0),20,0,0,0,0.45);
                    world.spawnParticle(Particle.FLASH,p.getEyeLocation().add(0, -1.75,0),1,0,0,0,0);
                    world.playSound(p.getEyeLocation().add(0,0,0), Sound.ENTITY_ZOMBIE_VILLAGER_CURE,1f,1.9f);
//        world.stopSound(Sound.BLOCK_PORTAL_TRAVEL);
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin,0,3);
        return true;
    }

    public static class TestSound implements CommandExecutor{
        JasperProject plugin;
        public TestSound(JasperProject plugin){
            this.plugin = plugin;
        }
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if(!(commandSender instanceof Player p)){
                commandSender.sendMessage("You cant do this with a console!");
                return true;
            }
            final int[] tes = {0};
            final float[] pitch = {0.4f};
            final float[] vel = {0.4f};
            new BukkitRunnable(){
                @Override
                public void run(){
//                    p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_HARP,10f, pitch[0]);
                    p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,10f, pitch[0]);
//                    p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_GUITAR,10f, pitch[0]);
                    if(pitch[0]>=1.4f | pitch[0]<0.4f){
                        vel[0] *=-1;
                    }
                        pitch[0] += vel[0];
                        p.sendMessage("pitch = "+pitch[0]);

                }
            }.runTaskTimerAsynchronously(plugin,0,2);

            return true;
        }
    }
}
