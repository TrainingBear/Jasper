package me.jasper.jasperproject.Dungeon.MapUtil;

import java.awt.Color;
import java.awt.Image;

import org.bukkit.map.MapFont;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MapCanvas {
    @NotNull
    MapView getMapView();

    @NotNull
    org.bukkit.map.MapCursorCollection getCursors();

    void setCursors(@NotNull MapCursorCollection var1);

    void setPixelColor(int var1, int var2, @Nullable Color var3);

    @Nullable
    Color getPixelColor(int var1, int var2);

    @NotNull
    Color getBasePixelColor(int var1, int var2);

    /** @deprecated */
    @Deprecated
    void setPixel(int var1, int var2, byte var3);

    /** @deprecated */
    @Deprecated
    byte getPixel(int var1, int var2);

    /** @deprecated */
    @Deprecated
    byte getBasePixel(int var1, int var2);

    void drawImage(int var1, int var2, @NotNull Image var3);

    void drawText(int var1, int var2, @NotNull MapFont var3, @NotNull String var4);
}
