package me.jasper.jasperproject.Bazaar.util;

import net.kyori.adventure.text.minimessage.MiniMessage;

public enum BazaarEnum {
    /// String as well as Categ
    SLIMEFUN_CATEG("<!i><color:#09ff00>Slimefun")
    /// String as well as Categ
    ,MOB_LOOT_CATEG("<!i><color:#b34a00>Mob Loot")
    /// String as well as Categ
    ,FARMING_CATEG("<!i><color:#FFD700>Farming")
    /// String as well as Categ
    ,MINING_CATEG("<!i><color:#78f5f5>Mining")
    /// String as well as Categ
    , FISHING_CATEG("<!i><color:#7663ff>Fishing")
    /// String as well as Categ
    ,MAGICAL_CATEG("<!i><color:#ff6edd>Magical")
    /// Component
    ,TITLE_NAME(MiniMessage.miniMessage().deserialize("<!italic><bold><gold>StashStocks"))
    /// Component
    ,CLOSE_ITEM(MiniMessage.miniMessage().deserialize("<!i><red><b>Close"))
    /// String
    ,TITLE_STRING("<!italic><bold><gold>StashStocks")
    /// String
    ,CLICK_TEXT("<!i><b><color:#77aa77>CLICK</b>");

    private Object term;
    BazaarEnum(Object term) {
        this.term = term;
    }

    BazaarEnum() {}

    /// MUST CAST AT CERTAIN CONDITION
    public Object get() {
        return term;
    }
}
