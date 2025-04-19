package me.jasper.jasperproject.Bazaar;

import me.jasper.jasperproject.Bazaar.Bazaar2.BazaarEnum;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.SignGUI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

public class BazaarListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e){
//        e.getWhoClicked().sendMessage(MiniMessage.miniMessage().serialize(e.getView().title())); //debug
        if(!MiniMessage.miniMessage().serialize(e.getView().title())
                .startsWith((String) BazaarEnum.TITLE_STRING.get())) return;
        e.setCancelled(true);

        try {//cmn failsafe       if statementny bs lu ubah/pindah
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(JKey.bazaar_Item_GUI, PersistentDataType.TAG_CONTAINER).has(JKey.bazaar_Action)) {
                switch (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(JKey.bazaar_Item_GUI, PersistentDataType.TAG_CONTAINER).get(JKey.bazaar_Action, PersistentDataType.STRING)) {
                    case "Close" -> e.getWhoClicked().closeInventory();
                    case "Search" ->{
                        String[] builtInText= {
                                ""
                                ,"^^^^^^^^^^^^"
                                ,"Search items"
                                ,""
                        };
                        SignGUI.getInstance().open((Player) e.getWhoClicked(),builtInText, Material.ACACIA_SIGN
                                ,(p, lines, signLoc) -> {
                                    p.sendBlockChange(signLoc, signLoc.getBlock().getBlockData());//turn back to normal

                        });
                    }
                    case "Categ:SF"-> Bazaar.setToSlimefunCateg(e.getInventory());
                    case "Categ:MobLoot"-> Bazaar.setToMobLootCateg(e.getInventory());
                    case "Categ:Farming"-> Bazaar.setToFarmingCateg(e.getInventory());
                    case "Categ:Mining" -> Bazaar.setToMiningCateg(e.getInventory());
                }
            }

        }catch (NullPointerException ignored) {}

    }
}
