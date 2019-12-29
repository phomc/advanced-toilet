package dev.anhcraft.advancedtoilet;

import co.aikar.commands.PaperCommandManager;
import dev.anhcraft.advancedtoilet.api.ToiletApi;
import dev.anhcraft.advancedtoilet.listeners.ShitHandler;
import dev.anhcraft.advancedtoilet.listeners.ToiletHandler;
import dev.anhcraft.advancedtoilet.tasks.MainTask;
import dev.anhcraft.advancedtoilet.tasks.RealisticControlTask;
import dev.anhcraft.craftkit.chat.Chat;
import dev.anhcraft.craftkit.utils.ServerUtil;
import dev.anhcraft.jvmkit.utils.FileUtil;
import dev.anhcraft.jvmkit.utils.IOUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class AdvancedToilet extends JavaPlugin {
    public Chat chat;
    public File dataFile;
    public YamlConfiguration dataConf;
    public YamlConfiguration generalConf;
    public YamlConfiguration messageConf;
    public boolean dataNeedSave;
    public ToiletHandler toiletHandler;
    public RealisticControlTask rct;
    public ToiletApi api;

    @Override
    public void onEnable() {
        loadConf();

        api = new ToiletApi(this);
        for(String k : dataConf.getKeys(false)){
            api.loadToilet(Integer.parseInt(k), Objects.requireNonNull(dataConf.getConfigurationSection(k)));
        }

        getServer().getPluginManager().registerEvents(new ShitHandler(), this);
        getServer().getPluginManager().registerEvents(toiletHandler = new ToiletHandler(this), this);

        getServer().getScheduler().runTaskTimer(this, new MainTask(this), 40, 20);
        getServer().getScheduler().runTaskTimer(this, rct = new RealisticControlTask(this), 40, 1200);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(dataNeedSave){
                    try {
                        dataConf.save(dataFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dataNeedSave = false;
                }
            }
        }.runTaskTimerAsynchronously(this, 0, 60);

        PaperCommandManager pcm = new PaperCommandManager(this);
        pcm.registerCommand(new ATCommand(this));
        pcm.enableUnstableAPI("help");
    }

    public void loadConf() {
        File folder = new File("plugins/Toilet");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File generalFile = new File(folder, "general.yml");
        File messageFile = new File(folder, "messages.yml");
        FileUtil.init(generalFile, () -> {
            try {
                return IOUtil.readResource(AdvancedToilet.class, "/general.yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        FileUtil.init(messageFile, () -> {
            try {
                return IOUtil.readResource(AdvancedToilet.class, "/messages.yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        try {
            (dataFile = new File(folder, "data.yml")).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataConf = YamlConfiguration.loadConfiguration(dataFile);
        generalConf = YamlConfiguration.loadConfiguration(generalFile);
        messageConf = YamlConfiguration.loadConfiguration(messageFile);

        chat = new Chat(generalConf.getString("prefix"));
    }

    @Override
    public void onDisable() {
        for (Entity e : ServerUtil.getAllEntities()) {
            if (!e.getType().equals(EntityType.DROPPED_ITEM) || !((Item) e).getItemStack().equals(api.getShit())) continue;
            e.remove();
        }
    }
}
