package dev.anhcraft.advancedtoilet.utils;

import dev.anhcraft.advancedtoilet.AdvancedToilet;
import dev.anhcraft.advancedtoilet.api.ToiletBowl;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;

public class BlockUpdater {
    public static void update(Block block, ToiletBowl.WaterLevel waterLevel) {
        if (block.getBlockData() instanceof Levelled) {
            Levelled lv = (Levelled) block.getBlockData();
            lv.setLevel(Math.min(lv.getMaximumLevel(), waterLevel.getLevel()));
            block.setBlockData(lv, false);
        } else {
            AdvancedToilet.getInstance().getLogger().warning("Incorrect block type: " + block.getType());
        }
    }
}
