package dev.anhcraft.advancedtoilet.api;

import dev.anhcraft.advancedtoilet.utils.BlockUpdater;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class ToiletBowl {
    private WaterLevel waterLevel;
    private Block block;

    public ToiletBowl(@NotNull WaterLevel waterLevel, @NotNull Block block) {
        this.block = block;
        this.waterLevel = waterLevel;

        update();
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
        BlockUpdater.update(block, waterLevel);
    }

    public enum WaterLevel {
        EMPTY(0),
        ONE_OF_THREE_PARTS(1),
        TWO_OF_THREE_PARTS(2),
        FULL(3);

        private final int level;

        WaterLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return this.level;
        }
    }
}
