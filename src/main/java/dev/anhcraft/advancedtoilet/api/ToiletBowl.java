package dev.anhcraft.advancedtoilet.api;

import dev.anhcraft.craftkit.cb_common.NMSVersion;
import org.bukkit.block.Block;
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
        if(NMSVersion.current().compare(NMSVersion.v1_13_R1) >= 0){
            org.bukkit.block.data.Levelled lv = (org.bukkit.block.data.Levelled) block.getBlockData();
            lv.setLevel(Math.min(lv.getMaximumLevel(), waterLevel.getLevel()));
            block.setBlockData(lv);
        } else {
            block.getState().getData().setData((byte) waterLevel.getLevel());
            block.getState().update();
        }
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
