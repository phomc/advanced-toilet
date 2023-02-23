package dev.anhcraft.advancedtoilet.utils;

import dev.anhcraft.jvmkit.utils.Condition;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public enum NMSVersion {
    v1_9_R2,
    v1_10_R1,
    v1_11_R1,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_14_R1,
    v1_15_R1,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_17_R1,
    v1_18_R1,
    v1_18_R2,
    v1_19_R1,
    v1_19_R2;

    private static final NMSVersion nms = NMSVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3]);

    public int compare(@NotNull NMSVersion another){
        Condition.argNotNull("another", another);
        return ordinal() - another.ordinal();
    }

    @NotNull
    public static NMSVersion current(){
        return nms;
    }
}
