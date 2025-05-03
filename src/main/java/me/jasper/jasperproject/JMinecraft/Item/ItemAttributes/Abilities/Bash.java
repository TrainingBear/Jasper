package me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.Abilities;

import lombok.Getter;
import lombok.Setter;
import me.jasper.jasperproject.JMinecraft.Item.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JMinecraft.Item.Util.Charge;
import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners.ArmorType;
import me.jasper.jasperproject.JMinecraft.Player.JPlayer;
import me.jasper.jasperproject.JMinecraft.Player.PlayerManager;
import me.jasper.jasperproject.JMinecraft.Player.Util.DamageType;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Bash extends ItemAbility {
    private static Bash instance;
    @Getter private static final Map<UUID, Float> powers = new HashMap<>();
    @Getter @Setter private boolean released;


    public static Bash getInstance() {
        if (instance == null) instance = new Bash();
        return instance;
    }

    public Bash() {
    }

    public Bash(int range, float cooldown) {
        this.setRange(range);
        this.setCooldown(cooldown);
    }

    public Bash(int range, float cooldown, Player p) {
        this.setRange(range);
        this.setCooldown(cooldown);
        this.setPlayer(p);
    }

    @EventHandler
    public void trigger(PlayerInteractEvent e) {
        if (!Util.hasAbility(e.getItem(), this.getKey())) return;
        if (TRIGGER.Interact.SNEAK_RIGHT_CLICK(e)) {
            PersistentDataContainer itemData = Util.getAbilityComp(e.getItem(), this.getKey());
            Bukkit.getPluginManager().callEvent(
                    new Bash(
                            itemData.get(JKey.key_range, PersistentDataType.INTEGER),
                            itemData.get(JKey.key_cooldown, PersistentDataType.FLOAT),
                            e.getPlayer()
                    )
            );
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void action(Bash e) {
        if(e.isCancelled()) return;
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if(hasCooldown(e)) return;
        if(e.isReleased()) {
            float power = powers.get(uuid);
            e.getPlayer().sendMessage("You have been released the power of "+power);
            bashAnimation(p, power, e);
            applyCooldown(e,  true);
            powers.remove(uuid);
            return;
        }

        Runnable onRelease = () -> {
            Bash event = (Bash) e.clone();
            event.setReleased(true);
            Bukkit.getPluginManager().callEvent(event);
        };

        BiConsumer<Long, BukkitRunnable> onTicking = (elapsed, on_release) -> {
            powers.put(uuid, powers.getOrDefault(uuid, 0f) + (float) elapsed/1000f);
            float power = powers.get(uuid);
            if(power > e.getRange()){
                on_release.runTask(JasperProject.getPlugin());
                return;
            }
            p.sendMessage("Charged "+power);
            p.playSound(p.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.PLAYERS, 1f, Math.min(2f, power * .4f));
        };

        HoldEvent holdEvent = new HoldEvent(p, onTicking, onRelease);
        Bukkit.getPluginManager().callEvent(holdEvent);
    }

    @Override
    protected List<Component> createLore() {
        return List.of(
                Util.deserialize("<!i><gold>Ability: <b><red>Bash <yellow>(HOLD SNEAK RIGHT CLICK)")
                ,Util.deserialize("<!i><gray>Release a smash on hit and deal")
                ,Util.deserialize("<!i><gray>10% damage to nearby entities<color:#95945B>HOLD</color> and")
                ,Util.deserialize("<!i><gray>SHIFT + RIGHT CLICK to store axe power!")
        );
    }

    private void basEM(Player p, float power, float max_power, Location hitLoc) {
        final float[] layout = {
                1,
                1.5f, 2, 3,
                4, 4.5f, 4.8f
        };
        final float range = p.isSwimming() ? layout[(int) Math.min(5f, power)] * .5f : layout[(int) Math.min(5f, power)];
        final BlockData blok = hitLoc.clone().add(0, -1, 0).getBlock().getBlockData();
        JPlayer jPlayer = PlayerManager.getJPlayer(p);

        for (LivingEntity entity : p.getLocation().getNearbyLivingEntities(range)) {
            if (entity instanceof Player ply) {
                ply.setVelocity(ply.getVelocity().add(new Vector(0, power * .075f, 0)));
                continue;
            }
            jPlayer.attack(entity, ArmorType.MAIN_HAND, DamageType.MELEE, false, power/max_power);
            final double yDiff = entity.getY() - p.getY();
            if (yDiff <= 2d && yDiff >= -1d) {
                entity.setVelocity(entity.getVelocity().add(new Vector(0, 0.06f * power + 0.25f, 0)));

                new BukkitRunnable(){
                    @Override public void run() {
                        entity.getWorld().spawnParticle(
                                Particle.BLOCK, entity.getLocation()
                                , 10, entity.getWidth() , 0 ,entity.getWidth(), .8f, blok
                        );

                        final double add = ThreadLocalRandom.current().nextDouble(-entity.getWidth(), entity.getWidth()+.0001);
                        for (byte j = 0; j < 9; j++) entity.getWorld().spawnParticle(
                                Particle.BLOCK
                                , entity.getLocation().add(add,add,add)
                                , 0, 0, 1, 0, 1f,
                                blok
                        );
                    }
                }.runTaskAsynchronously(JasperProject.getPlugin());
            }
        }
        float ratio = -0.07f * power + .85f;
        hitLoc.getWorld().playSound(
                hitLoc,Sound.ITEM_MACE_SMASH_GROUND
                ,SoundCategory.PLAYERS,ratio,ratio
                );

        float delta = .04f * power + .1f;
        hitLoc.getWorld().spawnParticle(
                Particle.BLOCK, hitLoc.clone().add(0,.2f,0)
                , (int) (9 * power + 5), delta , 0 ,delta, .5f, blok
        );

        short point = (short) (range * 7);
        for (short i = 0; i < point; i++) {
            final float angle = (float) (2 * Math.PI * i / point);
            Location particLoc = hitLoc.clone().add((Math.cos(angle) * range), .05f, (Math.sin(angle) * range));
            if (!particLoc.clone().add(0, -1, 0).getBlock().isSolid()) {
                particLoc.add(0, -1, 0);
                if (!particLoc.clone().add(0, -2, 0).getBlock().isSolid()) particLoc = null;
            } else if (particLoc.getBlock().getType().isSolid()) {
                particLoc.add(0, 1, 0);
                if (particLoc.getBlock().getType().isSolid()) particLoc.add(0, 1, 0);
            }
            if (particLoc != null) for (byte j = 0; j < 5; j++) p.getWorld().spawnParticle(
                        Particle.BLOCK
                        , particLoc
                        , 0, 0, 1, 0, 1f,
                        blok
                );
        }
    }

    private void bashAnimation(Player p, float power, Bash e) {
        Location pLoc = p.getLocation();
        pLoc.setPitch(0);
        Location eyeLoc = p.getEyeLocation().clone();
        eyeLoc.setPitch(0);

        Location hitLoc = pLoc.clone().add(pLoc.getDirection().normalize().multiply(.85f));
        if(!hitLoc.clone().add(0, -1, 0).getBlock().getType().isSolid()) hitLoc.add(0,-1,0);

        final float slisih = (float) ((eyeLoc.getY() - hitLoc.getY()) / 5);
        double[] multipliers = {0.52, 0.66, 0.78, 0.82, 0.87};
        final Vector vec = eyeLoc.getDirection().normalize();

        p.getWorld().playSound(
                p.getLocation(),Sound.ENTITY_PLAYER_ATTACK_SWEEP
                ,SoundCategory.PLAYERS, .42f,.5f
        );

        new BukkitRunnable() {
            byte frame = 0;
            @Override public void run() {
                if(!Util.hasAbility(p.getInventory().getItemInMainHand(), e.getKey())){
                    this.cancel();
                    return;
                }
                if(this.frame >= multipliers.length){
                    basEM(p, power, e.getRange(), hitLoc);
                    this.cancel();
                    return;
                }
                p.getWorld().spawnParticle(Particle.CRIT
                        , eyeLoc.clone().add(vec.clone().multiply(multipliers[this.frame]))
                                .add(0, -slisih * this.frame, 0)
                        , 5, 0.05f, 0.05f, 0.05f, 0);
                this.frame++;
            }
        }.runTaskTimer(JasperProject.getPlugin(),0, 0);
    }
}
