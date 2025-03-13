package me.jasper.jasperproject.Dungeon.MapUtil;

import java.util.List;
import org.bukkit.World;
import org.bukkit.map.MapRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MapView {
    int getId();

    boolean isVirtual();

    @NotNull
    Scale getScale();

    void setScale(@NotNull org.bukkit.map.MapView.Scale var1);

    int getCenterX();

    int getCenterZ();

    void setCenterX(int var1);

    void setCenterZ(int var1);

    @Nullable
    World getWorld();

    void setWorld(@NotNull World var1);

    @NotNull
    List<org.bukkit.map.MapRenderer> getRenderers();

    void addRenderer(@NotNull org.bukkit.map.MapRenderer var1);

    boolean removeRenderer(@Nullable MapRenderer var1);

    boolean isTrackingPosition();

    void setTrackingPosition(boolean var1);

    boolean isUnlimitedTracking();

    void setUnlimitedTracking(boolean var1);

    boolean isLocked();

    void setLocked(boolean var1);

    public static enum Scale {
        CLOSEST(0),
        CLOSE(1),
        NORMAL(2),
        FAR(3),
        FARTHEST(4);

        private byte value;

        private Scale(int value) {
            this.value = (byte)value;
        }

        /**
         * @deprecated
         */
        @Deprecated
        public static Scale valueOf(byte value) {
            switch (value) {
                case 0 -> {
                    return CLOSEST;
                }
                case 1 -> {
                    return CLOSE;
                }
                case 2 -> {
                    return NORMAL;
                }
                case 3 -> {
                    return FAR;
                }
                case 4 -> {
                    return FARTHEST;
                }
                default -> {
                    return null;
                }
            }
        }

        /** @deprecated */
        @Deprecated
        public byte getValue() {
            return this.value;
        }
    }
}
