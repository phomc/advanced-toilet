package dev.anhcraft.advancedtoilet.listeners;

import dev.anhcraft.advancedtoilet.api.ToiletApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ShitHandler implements Listener {
    @EventHandler
    public void pickup(EntityPickupItemEvent event) {
        if (event.getItem().getItemStack().equals(ToiletApi.getInstance().getShit())) {
            event.setCancelled(true);
        }
    }
}
