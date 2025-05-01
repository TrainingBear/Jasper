package me.jasper.jasperproject.JMinecraft.Player.EquipmentListeners;

import java.util.List;

import me.jasper.jasperproject.JMinecraft.Item.Util.TRIGGER;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;

/**
 * @author Arnah
 * @since Jul 30, 2015
 */
public class ArmorListener implements Listener{
    private final List<String> blockedMaterials;
    public ArmorListener(List<String> blockedMaterials){
        this.blockedMaterials = blockedMaterials;
    }
    //Event Priority is highest because other plugins might cancel the events before we check.

    @EventHandler(priority =  EventPriority.HIGHEST)
    public final void inventoryClick(final InventoryClickEvent e){
        boolean shift = false, numberkey = false;
        if(e.isCancelled()) return;
        if(e.getAction() == InventoryAction.NOTHING) return;
        if(e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
        if(e.getSlotType() != SlotType.ARMOR && e.getSlotType() != SlotType.QUICKBAR && e.getSlotType() != SlotType.CONTAINER) return;
        if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;

        if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)){
            shift = true;
        }
        if(e.getClick().equals(ClickType.NUMBER_KEY)){
            numberkey = true;
        }
        ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
        if(!shift && newArmorType != null && e.getRawSlot() != newArmorType.getSlot()){
            // Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots slot.
            return;
        }


        HumanEntity whoClicked = e.getWhoClicked();
        PlayerInventory inventory = whoClicked.getInventory();
        if(shift){
            newArmorType = ArmorType.matchType(e.getCurrentItem());
            if(newArmorType == null) return;
            boolean equipping = e.getRawSlot() != newArmorType.getSlot();
            if(
                    newArmorType.equals(ArmorType.HELMET) && (equipping == isAirOrNull(inventory.getHelmet())) ||
                    newArmorType.equals(ArmorType.CHESTPLATE) && (equipping == isAirOrNull(inventory.getChestplate())) ||
                    newArmorType.equals(ArmorType.LEGGINGS) && (equipping == isAirOrNull(inventory.getLeggings())) ||
                    newArmorType.equals(ArmorType.BOOTS) && (equipping == isAirOrNull(inventory.getBoots()))
            ){
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) whoClicked, ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if(armorEquipEvent.isCancelled()){
                    e.setCancelled(true);
                }
            }

        }else{
            ItemStack newArmorPiece = e.getCursor();
            ItemStack oldArmorPiece = e.getCurrentItem();
            if(numberkey){
                if(e.getClickedInventory().getType().equals(InventoryType.PLAYER)){// Prevents shit in the 2by2 crafting
                    // e.getClickedInventory() == The players inventory
                    // e.getHotBarButton() == key people are pressing to equip or unequip the item to or from.
                    // e.getRawSlot() == The slot the item is going to.
                    // e.getSlot() == Armor slot, can't use e.getRawSlot() as that gives a hotbar slot ;-;
                    ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
                    if(!isAirOrNull(hotbarItem)){// Equipping
                        newArmorType = ArmorType.matchType(hotbarItem);
                        newArmorPiece = hotbarItem;
                        oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
                    }else{// Unequipping
                        newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
                    }
                }
            }else{
                if(isAirOrNull(e.getCursor()) && !isAirOrNull(e.getCurrentItem())){// unequip with no new item going into the slot.
                    newArmorType = ArmorType.matchType(e.getCurrentItem());
                }
                // e.getCurrentItem() == Unequip
                // e.getCursor() == Equip
                // newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
            }
            if(newArmorType != null && e.getRawSlot() == newArmorType.getSlot()){
                ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
                if(e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) whoClicked, method, newArmorType, oldArmorPiece, newArmorPiece);
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if(armorEquipEvent.isCancelled()){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority =  EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent e){
        if(e.useItemInHand().equals(Result.DENY))return;
        if(e.getAction() == Action.PHYSICAL) return;
        if(TRIGGER.Interact.RIGHT_CLICK(e)){
            ItemStack armor = e.getItem();
            Player player = e.getPlayer();
            ArmorType newArmorType = ArmorType.matchType(armor);
            if(newArmorType == null) return;
            if(isAirOrNull(armor)){
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.matchType(armor), null, armor);
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if(armorEquipEvent.isCancelled()){
                    e.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
    public void inventoryDrag(InventoryDragEvent event){
        // getType() seems to always be even.
        // Old Cursor gives the item you are equipping
        // Raw slot is the ArmorType slot
        // Can't replace armor using this method making getCursor() useless.
        ArmorType type = ArmorType.matchType(event.getOldCursor());
        if(event.getRawSlots().isEmpty()) return;// Idk if this will ever happen
        if(type != null && type.getSlot() == event.getRawSlots().stream().findFirst().orElse(0)){
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, type, null, event.getOldCursor());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if(armorEquipEvent.isCancelled()){
                event.setResult(Result.DENY);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void itemBreakEvent(PlayerItemBreakEvent e){
        ArmorType type = ArmorType.matchType(e.getBrokenItem());
        if(type != null){
            Player p = e.getPlayer();
            ArmorEquipEvent armorEquipEvent =
                    new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), null);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if(armorEquipEvent.isCancelled()){
                ItemStack item = e.getBrokenItem().clone();
                item.setAmount(1);
                item.editMeta(meta->{
                    if(meta instanceof Damageable damageable){
                        damageable.setDamage(damageable.getDamage() - 1);
                    }
                });
                if(type.equals(ArmorType.HELMET)){
                    p.getInventory().setHelmet(item);
                }else if(type.equals(ArmorType.CHESTPLATE)){
                    p.getInventory().setChestplate(item);
                }else if(type.equals(ArmorType.LEGGINGS)){
                    p.getInventory().setLeggings(item);
                }else if(type.equals(ArmorType.BOOTS)){
                    p.getInventory().setBoots(item);
                }
            }
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e){
        Player p = e.getEntity();
        if(e.getKeepInventory()) return;
        for(ItemStack i : p.getInventory().getArmorContents()){
            if(!isAirOrNull(i)){
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, null));
            }
        }
    }

    /**
     * A utility method to support versions that use null or air ItemStacks.
     * @return return true if null
     */
    public static boolean isAirOrNull(ItemStack item){
        return item == null || item.getType().equals(Material.AIR);
    }
}

