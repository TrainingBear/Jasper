package me.jasper.jasperproject.JasperItem.Util;

import lombok.Getter;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ENCHANT;
import me.jasper.jasperproject.JasperItem.ItemAttributes.ItemAbility;
import me.jasper.jasperproject.JasperItem.Jitem;

import java.util.HashMap;
import java.util.HashSet;

public final class ItemHandler {
    @Getter
    private static HashSet<ItemAbility> abilities = new HashSet<>();
    //String = ID, Jitem = curent version.
    //brti ntar ngecek tiap item di inv player
    //sama get itemID nya. ntar nge retrive new versionya (Jitem)
    @Getter private static final HashMap<String, Jitem> items = new HashMap<>();
    @Getter
    private static final HashSet<ENCHANT> enchants = new HashSet<>();
}
