package dev.anhcraft.advancedtoilet.utils;

import dev.anhcraft.advancedtoilet.api.Toilet;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class CustomHolder implements InventoryHolder {
    private Inventory inv;
    private final Toilet toilet;

    public CustomHolder(Toilet toilet) {
        this.toilet = toilet;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void setInventory(Inventory inv) {
        this.inv = inv;
    }

    public Toilet getToilet() {
        return toilet;
    }
}
