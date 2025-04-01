package me.jasper.jasperproject.JasperItem.ItemAttributes;

public enum ItemType {
    ARMOR,
    SWORD,
    LONGSWORD,
    BOW,
    SHORTBOW,
    LONGBOW,
    AXE,
    MACE,
    TRIDENT,
    PICKAXE,
    SHOVEL,
    ROD,
    ITEM;

    public static ItemType getFromString(String string){
        return switch (string){
            case "ARMOR" -> ARMOR;
            case "SWORD" -> SWORD;
            case "LONGSWORD" -> LONGSWORD;
            case "BOW" -> BOW;
            case "SHORTBOW" -> SHORTBOW;
            case "LONGBOW" -> LONGBOW;
            case "AXE" -> AXE;
            case "MACE" -> MACE;
            case "TRIDENT" -> TRIDENT;
            case "PICKAXE" -> PICKAXE;
            case "SHOVEL" -> SHOVEL;
            case "ROD" -> ROD;
            default -> ITEM;
        };
    }

}
