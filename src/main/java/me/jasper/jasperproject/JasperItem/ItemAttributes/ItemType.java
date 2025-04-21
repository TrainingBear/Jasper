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
    WAND,
    STAFF,
    INGREDIENTS, //bahan crafting yg sebelumnya udh di craft,
    // kayak material di craft jadi ingredient, ingredient bisa di craft lg jd bahan ingredient/matang
    MATERIAL, //bahan mentah, sementah-mentahnya :moyai:
    ITEM,
    HELMET,
    GOOGLES,
    CHESTPLATE,
    LEGGINGS,
    BOOTS;

    public static ItemType getFromString(String string){
        try{
            return ItemType.valueOf(string);
        }catch(IllegalArgumentException e) {
            return ITEM;
        }
    }

}
