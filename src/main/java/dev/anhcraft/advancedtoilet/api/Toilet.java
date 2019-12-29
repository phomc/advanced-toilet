package dev.anhcraft.advancedtoilet.api;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Toilet {
    private int id;
    private Location spawnPoint;
    private ToiletBowl bowl;
    private Block door;
    private ToiletPassenger passenger;

    public Toilet(int id, @NotNull Location spawnPoint) {
        this.spawnPoint = spawnPoint;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public Location getSpawnPoint() {
        return this.spawnPoint;
    }

    public void setSpawnPoint(@NotNull Location spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    @Nullable
    public ToiletBowl getBowl() {
        return bowl;
    }

    public void setBowl(@Nullable ToiletBowl bowl) {
        this.bowl = bowl;
    }

    @Nullable
    public Block getDoor() {
        return door;
    }

    public void setDoor(@Nullable Block door) {
        this.door = door;
    }

    @Nullable
    public ToiletPassenger getPassenger() {
        return passenger;
    }

    public void setPassenger(@Nullable ToiletPassenger passenger) {
        this.passenger = passenger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Toilet toilet = (Toilet) o;
        return id == toilet.id &&
                spawnPoint.equals(toilet.spawnPoint) &&
                Objects.equals(bowl, toilet.bowl) &&
                Objects.equals(door, toilet.door);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
