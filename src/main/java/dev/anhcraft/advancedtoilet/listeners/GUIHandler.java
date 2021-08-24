package dev.anhcraft.advancedtoilet.listeners;

import dev.anhcraft.advancedtoilet.ATComponent;
import dev.anhcraft.advancedtoilet.AdvancedToilet;
import dev.anhcraft.advancedtoilet.api.ToiletActivity;
import dev.anhcraft.advancedtoilet.utils.CustomHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIHandler extends ATComponent implements Listener {
    public GUIHandler(AdvancedToilet plugin) {
        super(plugin);
    }

    @EventHandler
    public void click(InventoryClickEvent event) {
        if(event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof CustomHolder) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            if(event.getSlot() == 11) {
                plugin.toiletHandler.newActive((Player) event.getWhoClicked(),
                        ((CustomHolder) event.getClickedInventory().getHolder()).getToilet(),
                        ToiletActivity.PEE);
            } else if(event.getSlot() == 15) {
                plugin.toiletHandler.newActive((Player) event.getWhoClicked(),
                        ((CustomHolder) event.getClickedInventory().getHolder()).getToilet(),
                        ToiletActivity.POOP);
            }
        }
    }
}
