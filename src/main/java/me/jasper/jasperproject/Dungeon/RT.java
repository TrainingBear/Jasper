package me.jasper.jasperproject.Dungeon;

import org.bukkit.Material;

public enum RT {
    START {
        @Override
        public Material getType(){
            return Material.GREEN_WOOL;
        }

        @Override
        public Integer getLimit() {
            return 0;
        }
    },
    BLOOD{
        @Override
        public Material getType(){
            return Material.RED_WOOL;
        }

        @Override
        public Integer getLimit() {
            return 0;
        }
    },
    MINI_BOSS{
        @Override
        public Integer getLimit() {
            return 0;
        }

        @Override
        public Material getType(){
            return Material.YELLOW_WOOL;
        }
    },
    PUZLE{
        @Override
        public Material getType(){
            return Material.PURPLE_WOOL;
        }

        @Override
        public Integer getLimit() {
            return 0;
        }
    },
    FAIRY{
        @Override
        public Material getType(){
            return Material.PINK_WOOL;
        }

        @Override
        public Integer getLimit() {
            return 0;
        }
    },
    PUZLE2{
        @Override
        public Material getType(){
            return Material.LIGHT_BLUE_WOOL;
        }

        @Override
        public Integer getLimit() {
            return 0;
        }
    },
    TRAP{
        @Override
        public Material getType(){
            return Material.ORANGE_WOOL;
        }

        @Override
        public Integer getLimit() {
            return 0;
        }
    },
    a1X1{
        @Override
        public Material getType(){
            return Material.OAK_LOG;
        }

        @Override
        public Integer getLimit() {
            return 2;
        }
    },
    a2X1{
        @Override
        public Material getType(){
            return Material.OAK_FENCE;
        }

        @Override
        public Integer getLimit() {
            return 2;
        }
    },
    a3X1{
        @Override
        public Material getType(){
            return Material.NETHER_BRICK_FENCE;
        }

        @Override
        public Integer getLimit() {
            return 1;
        }
    },
    aL{
        @Override
        public Material getType(){
            return Material.BLACKSTONE_WALL;
        }

        @Override
        public Integer getLimit() {
            return 1;
        }
    },
    a2X2{
        @Override
        public Material getType(){
            return Material.MAGENTA_GLAZED_TERRACOTTA;
        }

        @Override
        public Integer getLimit() {
            return 1;
        }
    },
    a4X1{
        @Override
        public Material getType(){
            return Material.REINFORCED_DEEPSLATE;
        }

        @Override
        public Integer getLimit() {
            return 1;
        }
    };

    public abstract Material getType();
    public abstract Integer getLimit();
}
