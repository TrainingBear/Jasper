package me.jasper.jasperproject.JasperItem.ItemAttributes;

public class GemstoneAttribute{
    Rarity rarity;
    gemType type;
    public GemstoneAttribute(gemType type ,Rarity rarity){
        this.rarity = rarity;
        this.type =type;
    }
    public enum gemType{
        JASPER_GEMSTONE("jasper");

        private final String type;
        gemType(String type){
            this.type = type;
        }

    }
}

