package dev.anhcraft.advancedtoilet.api;

import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.jetbrains.annotations.NotNull;

public class ToiletBowl {
    private WaterLevel waterLevel;
    private Block block;

    public ToiletBowl(@NotNull WaterLevel waterLevel, @NotNull Block block) {
        this.block = block;
        this.waterLevel = waterLevel;
    }

    public void setBlock(@NotNull Block block) {
        this.block = block;
    }

    @NotNull
    public Block getBlock() {
        return block;
    }

    @NotNull
    public WaterLevel getWaterLevel() {
        return this.waterLevel;
    }

    public void setWaterLevel(@NotNull WaterLevel waterLevel) {
        this.waterLevel = waterLevel;
    }

    public void update() {
        Levelled lv = (Levelled) block.getBlockData();
        lv.setLevel(Math.min(lv.getMaximumLevel(), waterLevel.getLevel()));
        block.setBlockData(lv);
    }

    public enum WaterLevel {
        EMPTY(0),
        ONE_OF_THREE_PARTS(1),
        TWO_OF_THREE_PARTS(2),
        FULL(3);

        private int level;

        WaterLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return this.level;
        }
    }
}
