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
        for (UUID u : ToiletHandler.USING_TOILET.keySet()) {
            ToiletPassenger ta = ToiletHandler.USING_TOILET.get(u);
            if (ta.getTime() == ta.getMaxTime()) {
                ta.getToilet().setPassenger(null);
                ToiletHandler.USING_TOILET.remove(u);
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
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            tb.setWaterLevel(ToiletBowl.WaterLevel.ONE_OF_THREE_PARTS);
                            tb.update();
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                tb.setWaterLevel(ToiletBowl.WaterLevel.EMPTY);
                                tb.update();
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                    tb.setWaterLevel(ToiletBowl.WaterLevel.ONE_OF_THREE_PARTS);
                                    tb.update();
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        tb.setWaterLevel(ToiletBowl.WaterLevel.TWO_OF_THREE_PARTS);
                                        tb.update();
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                            tb.setWaterLevel(ToiletBowl.WaterLevel.FULL);
                                            tb.update();
                                        }, 30L);
                                    }, 30L);
                                }, 30L);
                            }, 30L);
                        }, 30L);
                    }, 30L);
                }
                continue;
            }
            ta.setTime(ta.getTime() + 1);
            if (Math.random() < plugin.generalConf.getDouble("counter_inc_chance")) {
                Location l = ta.getPlayer().getLocation();
                World w = Objects.requireNonNull(l.getWorld());
                if (ta.getActivity().equals(ToiletActivity.PEE)) {
                    w.playSound(l, Sound.BLOCK_LAVA_POP, 3.0f, 0.5f);
                    w.spawnParticle(Particle.DRIP_WATER, l, 300, 0.5, 0.5, 0.5, 0);
                } else {
                    w.playSound(l, Sound.BLOCK_LAVA_POP, 3.0f, 0.5f);
                    w.spawnParticle(Particle.DRIP_LAVA, l, 300, 0.5, 0.5, 0.5, 0);
                }
                ta.setCounter(ta.getCounter() + 1);
            }
            ToiletHandler.USING_TOILET.put(u, ta);
        }
    }
}
