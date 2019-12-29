package dev.anhcraft.advancedtoilet.tasks;

import dev.anhcraft.advancedtoilet.ATComponent;
import dev.anhcraft.advancedtoilet.AdvancedToilet;
import dev.anhcraft.advancedtoilet.api.ToiletPassenger;
import dev.anhcraft.advancedtoilet.api.ToiletActivity;
import dev.anhcraft.advancedtoilet.api.ToiletBowl;
import dev.anhcraft.advancedtoilet.listeners.ToiletHandler;
import org.bukkit.*;

import java.util.Objects;
import java.util.UUID;

public class MainTask extends ATComponent implements Runnable {
    public MainTask(AdvancedToilet plugin) {
        super(plugin);
    }

    @Override
    public void run() {
        for (UUID u : ToiletHandler.usingToilet.keySet()) {
            ToiletPassenger ta = ToiletHandler.usingToilet.get(u);
            if (ta.getTime() == ta.getMaxTime()) {
                ta.getPlayer().teleport(ta.getToilet().getSpawnPoint());
                ta.getToilet().setPassenger(null);
                ToiletHandler.usingToilet.remove(u);
                if (ta.getActivity().equals(ToiletActivity.PEE)) {
                    plugin.chat.message(ta.getPlayer(), Objects.requireNonNull(plugin.messageConf.getString("pee_show_analytics")).replace("{data}", Integer.toString(ta.getCounter())).replace("{time}", Integer.toString(ta.getTime())));
                } else {
                    plugin.chat.message(ta.getPlayer(), Objects.requireNonNull(plugin.messageConf.getString("poop_show_analytics")).replace("{data}", Integer.toString(ta.getCounter())).replace("{time}", Integer.toString(ta.getTime())));
                };
                ToiletBowl tb = ta.getToilet().getBowl();
                if(tb != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        tb.setWaterLevel(ToiletBowl.WaterLevel.TWO_OF_THREE_PARTS);
                        tb.update();
                    }, 30L);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        tb.setWaterLevel(ToiletBowl.WaterLevel.ONE_OF_THREE_PARTS);
                        tb.update();
                    }, 45L);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        tb.setWaterLevel(ToiletBowl.WaterLevel.EMPTY);
                        tb.update();
                    }, 60L);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        tb.setWaterLevel(ToiletBowl.WaterLevel.FULL);
                        tb.update();
                    }, 80L);
                }
                continue;
            }
            ta.setTime(ta.getTime() + 1);
            if (Math.random() < plugin.generalConf.getDouble("counter_inc_chance")) {
                Location l = ta.getPlayer().getLocation();
                World w = Objects.requireNonNull(l.getWorld());
                if (ta.getActivity().equals(ToiletActivity.PEE)) {
                    w.playSound(l, Sound.BLOCK_LAVA_POP, 3.0f, 0.5f);
                    w.spawnParticle(Particle.DRIP_WATER, l, 300, 0, 0, 0, 0);
                } else {
                    w.spawnParticle(Particle.DRIP_LAVA, l, 300, 0, 0, 0, 0);
                }
                ta.setCounter(ta.getCounter() + 1);
            }
            ToiletHandler.usingToilet.put(u, ta);
        }
    }
}
