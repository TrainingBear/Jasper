package me.jasper.jasperproject.Bazaar;

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
    /// Component
    ,TITLE_NAME_COMPO(MiniMessage.miniMessage().deserialize("<!italic><bold><gold>StashStocks")),
    /// Component
    CLOSE_ITEM(MiniMessage.miniMessage().deserialize("<!i><red><b>Close"))
    /// String
    ,TITLE_STRING("<!italic><bold><gold>StashStocks")
    /// String
    ,CLICK_TEXT("<b><color:#77aa77>CLICK</b>");

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
