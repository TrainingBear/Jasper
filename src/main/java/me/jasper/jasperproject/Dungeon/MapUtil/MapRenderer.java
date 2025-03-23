package me.jasper.jasperproject.Dungeon.MapUtil;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class MapRenderer {
    private boolean contextual;

    public MapRenderer() {
        this(false);
    }

    public MapRenderer(boolean contextual) {
        this.contextual = contextual;
    }

    public final boolean isContextual() {
        return this.contextual;
    }

    public void initialize(@NotNull MapView map) {
    }

    public abstract void render(@NotNull MapView mapView, @NotNull me.jasper.jasperproject.Dungeon.MapUtil.MapCanvas mapCanvas, @NotNull Player player);
}
