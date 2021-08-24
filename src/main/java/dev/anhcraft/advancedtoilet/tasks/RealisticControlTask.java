package dev.anhcraft.advancedtoilet.tasks;

import dev.anhcraft.advancedtoilet.ATComponent;
import dev.anhcraft.advancedtoilet.AdvancedToilet;
import dev.anhcraft.advancedtoilet.api.ToiletActivity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class RealisticControlTask extends ATComponent implements Runnable {
    public Map<UUID, Integer> peeTempMap = new HashMap<>();
    public Map<UUID, Integer> poopTempMap = new HashMap<>();

    public RealisticControlTask(AdvancedToilet plugin) {
        super(plugin);
    }

    private void addTime(Player p, ToiletActivity act) {
        UUID u = p.getUniqueId();
        if (act.equals(ToiletActivity.PEE)) {
            if (peeTempMap.containsKey(u)) {
                peeTempMap.put(u, peeTempMap.get(u) + 1);
            } else {
                peeTempMap.put(u, 0);
            }
        } else if (poopTempMap.containsKey(u)) {
            poopTempMap.put(u, poopTempMap.get(u) + 1);
        } else {
            poopTempMap.put(u, 0);
        }
    }

    public void resetTime(Player p, ToiletActivity act) {
        UUID u = p.getUniqueId();
        if (act.equals(ToiletActivity.PEE)) {
            peeTempMap.remove(u);
        } else {
            poopTempMap.remove(u);
        }
    }

    private int getTime(Player p, ToiletActivity act) {
        UUID u = p.getUniqueId();
        if (act.equals(ToiletActivity.PEE)) {
            return peeTempMap.getOrDefault(u, 0);
        }
        return poopTempMap.getOrDefault(u, 0);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            int time;
            int max;
            if (plugin.generalConf.getBoolean("realistic_mode.pee.enabled")) {
                time = getTime(p, ToiletActivity.PEE);
                if (plugin.generalConf.getInt("realistic_mode.pee.interval.max") < time) {
                    resetTime(p, ToiletActivity.PEE);
                    plugin.chat.message(p, Objects.requireNonNull(plugin.messageConf.getString("pee_through_time")));
                    p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation(), 300, 0, 0, 0, 0);
                } else {
                    addTime(p, ToiletActivity.PEE);
                    time = getTime(p, ToiletActivity.PEE);
                    max = plugin.generalConf.getInt("realistic_mode.pee.interval.max");
                    if (time == max - 1 || time == max - 3 || time == max - 5) {
                        plugin.chat.message(p, Objects.requireNonNull(plugin.messageConf.getString("need_pee")));
                    }
                }
            }
            if (plugin.generalConf.getBoolean("realistic_mode.poop.enabled")) {
                time = getTime(p, ToiletActivity.POOP);
                if (plugin.generalConf.getInt("realistic_mode.poop.interval.max") < time) {
                    resetTime(p, ToiletActivity.POOP);
                    plugin.chat.message(p, Objects.requireNonNull(plugin.messageConf.getString("poop_through_time")));
                    p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation(), 300, 0, 0, 0, 0);
                    final Item i = p.getWorld().dropItemNaturally(p.getLocation(), plugin.api.getShit());
                    i.setCustomName(ChatColor.translateAlternateColorCodes('&', p.getName() + "'s shit"));
                    i.setCustomNameVisible(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, i::remove, 120L);
                    continue;
                }
                addTime(p, ToiletActivity.POOP);
                time = getTime(p, ToiletActivity.POOP);
                max = plugin.generalConf.getInt("realistic_mode.poop.interval.max");
                if (time == max - 1 || time == max - 3) {
                    plugin.chat.message(p, Objects.requireNonNull(plugin.messageConf.getString("need_poop")));
                    continue;
                }
                if (time != max - 5) continue;
                plugin.chat.message(p, Objects.requireNonNull(plugin.messageConf.getString("need_poop")));
            }
        }
    }
}
