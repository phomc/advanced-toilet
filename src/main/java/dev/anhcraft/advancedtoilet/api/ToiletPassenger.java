package dev.anhcraft.advancedtoilet.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToiletPassenger {
    private final Toilet toilet;
    private int time;
    private final ToiletActivity act;
    private int counter;
    private final int maxTime;
    private final Player player;

    public ToiletPassenger(@NotNull Player player, @NotNull ToiletActivity act, int time, int maxTime, @NotNull Toilet toilet) {
        this.toilet = toilet;
        this.time = time;
        this.act = act;
        this.maxTime = maxTime;
        this.player = player;
    }

    @NotNull
    public Toilet getToilet() {
        return this.toilet;
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMaxTime() {
        return this.maxTime;
    }

    @NotNull
    public ToiletActivity getActivity() {
        return this.act;
    }

    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}

