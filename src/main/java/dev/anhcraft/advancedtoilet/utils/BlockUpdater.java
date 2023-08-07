package dev.anhcraft.advancedtoilet.utils;

import dev.anhcraft.advancedtoilet.api.ToiletBowl;
import org.bukkit.block.Block;

public class BlockUpdater {
    public static void update(Block block, ToiletBowl.WaterLevel waterLevel) {
        org.bukkit.block.data.Levelled lv = (org.bukkit.block.data.Levelled) block.getBlockData();
        lv.setLevel(Math.min(lv.getMaximumLevel(), waterLevel.getLevel()));
        block.setBlockData(lv, false);
    }
}
