package dev.anhcraft.advancedtoilet.utils;

import dev.anhcraft.advancedtoilet.api.ToiletBowl;
import org.bukkit.block.Block;

public class LegacyBlockUpdater {
    public static void update(Block block, ToiletBowl.WaterLevel waterLevel) {
        block.getState().getData().setData((byte) waterLevel.getLevel());
        block.getState().update();
    }
}
