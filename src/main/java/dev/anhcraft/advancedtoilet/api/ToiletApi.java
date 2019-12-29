/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.anhcraft.spaciouslib.Converter.DataTypes
 *  org.anhcraft.spaciouslib.Inventory.SItems
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 */
package dev.anhcraft.advancedtoilet.api;

import com.google.common.collect.ImmutableList;
import dev.anhcraft.advancedtoilet.ATComponent;
import dev.anhcraft.advancedtoilet.AdvancedToilet;
import dev.anhcraft.craftkit.builders.ItemBuilder;
import dev.anhcraft.jvmkit.utils.EnumUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class ToiletApi extends ATComponent {
    private static Map<Integer, Toilet> DATA = new HashMap<>();
    private static ToiletApi instance;

    @NotNull
    public static ToiletApi getInstance(){
        if(instance == null){
            throw new IllegalStateException("API is not ready");
        }
        return instance;
    }

    public ToiletApi(AdvancedToilet plugin) {
        super(plugin);
        instance = this;
    }

    public void setToilet(Toilet t) {
        DATA.put(t.getId(), t);
        saveToilet(t);
    }

    @Nullable
    public Toilet getToilet(int id){
        return DATA.get(id);
    }

    @NotNull
    public Optional<Toilet> findToilet(@NotNull Predicate<Toilet> filter){
        return DATA.values().stream().filter(filter).findAny();
    }

    @NotNull
    public List<Toilet> getAllToilets(){
        return ImmutableList.copyOf(DATA.values());
    }

    public void destroyToilet(int id){
        if(DATA.remove(id) != null){
            plugin.dataConf.set(String.valueOf(id), null);
            plugin.dataNeedSave = true;
        }
    }

    public void loadToilet(int id, ConfigurationSection section) {
        Toilet toilet = new Toilet(id, (Location) Objects.requireNonNull(section.get("spawnPoint")));
        if (section.isSet("bowl")) {
            ToiletBowl tb = new ToiletBowl(
                    (ToiletBowl.WaterLevel) Objects.requireNonNull(EnumUtil.findEnum(ToiletBowl.WaterLevel.class,
                            Objects.requireNonNull(section.getString("bowl.waterLevel")))),
                    ((Location) Objects.requireNonNull(section.get("bowl.location"))).getBlock()
            );
            toilet.setBowl(tb);
        }
        if (section.isSet("door")) {
            toilet.setDoor(((Location) Objects.requireNonNull(section.get("door"))).getBlock());
        }
        DATA.put(id, toilet);
    }

    public void saveToilet(Toilet x){
        YamlConfiguration c = new YamlConfiguration();
        c.set("spawnPoint", x.getSpawnPoint());
        if(x.getBowl() != null){
            c.set("bowl.location", x.getBowl().getBlock().getLocation());
            c.set("bowl.waterLevel", x.getBowl().getWaterLevel().name());
        }
        if(x.getDoor() != null){
            c.set("door", x.getDoor().getLocation());
        }
        plugin.dataConf.set(String.valueOf(x.getId()), c);
        plugin.dataNeedSave = true;
    }

    @NotNull
    public ItemStack getShit() {
        return new ItemBuilder(Material.COARSE_DIRT).name(plugin.generalConf.getString("shit_name")).build();
    }
}

