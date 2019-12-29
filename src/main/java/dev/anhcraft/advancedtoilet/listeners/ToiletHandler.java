package dev.anhcraft.advancedtoilet.listeners;

import dev.anhcraft.advancedtoilet.ATComponent;
import dev.anhcraft.advancedtoilet.AdvancedToilet;
import dev.anhcraft.advancedtoilet.api.ToiletPassenger;
import dev.anhcraft.advancedtoilet.api.Toilet;
import dev.anhcraft.advancedtoilet.api.ToiletActivity;
import dev.anhcraft.advancedtoilet.api.ToiletBowl;
import dev.anhcraft.craftkit.CraftExtension;
import dev.anhcraft.craftkit.builders.ItemBuilder;
import dev.anhcraft.craftkit.cb_common.callbacks.gui.SlotCallback;
import dev.anhcraft.craftkit.cb_common.gui.BaseGUI;
import dev.anhcraft.craftkit.cb_common.gui.CustomGUI;
import dev.anhcraft.craftkit.cb_common.inventory.CenterSlot;
import dev.anhcraft.craftkit.common.utils.ChatUtil;
import dev.anhcraft.craftkit.utils.InventoryUtil;
import dev.anhcraft.jvmkit.utils.RandomUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ToiletHandler extends ATComponent implements Listener {
    public static Map<UUID, ToiletPassenger> usingToilet = new HashMap<>();

    public ToiletHandler(AdvancedToilet plugin) {
        super(plugin);
    }

    public void newActive(Player p, Toilet toilet, ToiletActivity act) {
        plugin.rct.resetTime(p, act);
        ToiletBowl tb = toilet.getBowl();
        if(tb != null) {
            p.teleport(tb.getBlock().getLocation().add(0, 1, 0));
            tb.setWaterLevel(ToiletBowl.WaterLevel.FULL);
            tb.update();
        }
        int min = plugin.generalConf.getInt("time_wc.min");
        int max = plugin.generalConf.getInt("time_wc.max");
        ToiletPassenger ta = new ToiletPassenger(p, act, 0, RandomUtil.randomInt(min, max), toilet);
        toilet.setPassenger(ta);
        usingToilet.put(p.getUniqueId(), ta);
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        ToiletPassenger x = usingToilet.get(p.getUniqueId());
        if (x != null) {
            ToiletBowl tb = x.getToilet().getBowl();
            if(tb == null) return;
            double x1 = event.getFrom().getBlockX();
            double z1 = event.getFrom().getBlockZ();
            double x2 = Objects.requireNonNull(event.getTo()).getBlockX();
            double z2 = event.getTo().getBlockZ();
            if (x1 != x2 || z1 != z2) {
                if (x.getActivity().equals(ToiletActivity.PEE)) {
                    plugin.chat.message(p, Objects.requireNonNull(plugin.messageConf.getString("peeing_leave")));
                } else {
                    plugin.chat.message(p, Objects.requireNonNull(plugin.messageConf.getString("pooping_leave")));
                }
                Location tp = tb.getBlock().getLocation();
                tp.setPitch(p.getLocation().getPitch());
                tp.setYaw(p.getLocation().getYaw());
                p.teleport(tp.add(0, 1, 0));
            }
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock != null) {
            ToiletPassenger iut = usingToilet.get(event.getPlayer().getUniqueId());
            if(iut == null){
                Location clickedLoc = clickedBlock.getLocation();
                if (clickedBlock.getType().equals(Material.CAULDRON)) {
                    Toilet t = null;
                    for (Toilet x : plugin.api.getAllToilets()) {
                        if(x.getBowl() == null) continue;
                        Location loc = x.getBowl().getBlock().getLocation();
                        if (!loc.equals(clickedBlock.getLocation())) continue;
                        t = x;
                        break;
                    }
                    if (t != null) {
                        if(t.getPassenger() == null){
                            this.toiletActivityMenu(event.getPlayer(), t);
                        } else {
                            event.setCancelled(true);
                        }
                    }
                } else if (clickedBlock.getType().name().contains("_DOOR")) {
                    boolean selected = false;
                    for (ToiletPassenger ta : usingToilet.values()) {
                        Block dr = ta.getToilet().getDoor();
                        if(dr == null) continue;
                        Location loc = dr.getLocation();
                        if (loc.equals(clickedLoc)) {
                            selected = true;
                            break;
                        }
                        Block b1 = clickedBlock.getRelative(BlockFace.UP);
                        if (b1.getType() == dr.getType() && b1.getLocation().equals(clickedLoc)) {
                            selected = true;
                            break;
                        }
                        Block b2 = clickedBlock.getRelative(BlockFace.DOWN);
                        if (b2.getType() == dr.getType() && b2.getLocation().equals(clickedLoc)) {
                            selected = true;
                            break;
                        }
                    }
                    if (selected) {
                        event.setCancelled(true);
                        plugin.chat.message(event.getPlayer(), Objects.requireNonNull(plugin.messageConf.getString("open_active_toilet_door")));
                    }
                }
            } else {
                if (clickedBlock.getType().equals(Material.CAULDRON)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void toiletActivityMenu(Player p, Toilet toilet) {
        CustomGUI cg = CraftExtension.of(AdvancedToilet.class).createCustomGUI(null,
                27,
                ChatUtil.formatColorCodes(plugin.messageConf.getString("title_wc_gui"))
        );
        InventoryUtil.fillAll(cg, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        cg.addContentCallback(SlotCallback.PREVENT_MODIFY);
        cg.setItem(CenterSlot.CENTER_2_A.row(1), new ItemBuilder(Material.DIAMOND).name(plugin.messageConf.getString("pee_wc_gui")).build(), new SlotCallback() {
            @Override
            public void click(InventoryClickEvent event, Player player, BaseGUI gui) {
                plugin.toiletHandler.newActive(player,
                        toilet,
                        ToiletActivity.PEE);
            }
        });
        cg.setItem(CenterSlot.CENTER_2_B.row(1), new ItemBuilder(Material.DIAMOND).name(plugin.messageConf.getString("poop_wc_gui")).build(), new SlotCallback() {
            @Override
            public void click(InventoryClickEvent event, Player player, BaseGUI gui) {
                plugin.toiletHandler.newActive(player,
                        toilet,
                        ToiletActivity.POOP);
            }
        });
        p.openInventory(cg);
    }
}
