package dev.anhcraft.advancedtoilet;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import dev.anhcraft.advancedtoilet.api.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@CommandAlias("at|wc|toilet")
public class ATCommand extends BaseCommand {
    private final AdvancedToilet plugin;

    public ATCommand(AdvancedToilet plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    @CatchUnknown
    public void help(CommandSender sender, CommandHelp help){
        help.showHelp();
    }

    @Subcommand("reload")
    @Description("Reload the configuration")
    @CommandPermission("at.reload")
    public void reload(CommandSender sender){
        plugin.loadConf();
        plugin.chat.message(sender, Objects.requireNonNull(plugin.messageConf.getString("reloaded")));
    }

    @Subcommand("toilet create")
    @Description("Create a toilet (and set the spawn)")
    @CommandPermission("at.toilet.create")
    public void createToilet(Player player, int id){
        Toilet t = plugin.api.getToilet(id);
        if(t != null){
            plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("exits_toilet")));
            return;
        }
        t = new Toilet(id, player.getLocation());
        plugin.api.setToilet(t);
        plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("done")));
    }

    @Subcommand("toilet setspawn")
    @Description("Set spawn for a toilet")
    @CommandPermission("at.toilet.setspawn")
    public void setToiletSpawn(Player player, int id){
        Toilet t = plugin.api.getToilet(id);
        if(t == null){
            plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("not_exits_toilet")));
            return;
        }
        t.setSpawnPoint(player.getLocation());
        plugin.api.saveToilet(t);
        plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("done")));
    }

    @Subcommand("toilet setbowl")
    @Description("Set the toilet bowl under you")
    @CommandPermission("at.toilet.setbowl")
    public void setToiletBowl(Player player, int id){
        Location l = player.getLocation().subtract(0, 1, 0);
        if (l.getBlock().getType().equals(Material.CAULDRON)) {
            Toilet t = plugin.api.getToilet(id);
            if(t == null){
                plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("not_exits_toilet")));
                return;
            }
            ToiletBowl tb = t.getBowl();
            if(tb == null) {
                t.setBowl(new ToiletBowl(ToiletBowl.WaterLevel.FULL, l.getBlock()));
            } else {
                tb.setBlock(l.getBlock());
            }
            plugin.api.saveToilet(t);
            plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("done")));
        } else {
            plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("bowl_wrong_material")));
        }
    }

    @Subcommand("toilet setdoor")
    @Description("Set the door under you")
    @CommandPermission("at.toilet.setdoor")
    public void setToiletDoor(Player player, int id){
        Location l = player.getLocation().subtract(0, 1, 0);
        if (l.getBlock().getType().name().contains("_DOOR")) {
            Toilet t = plugin.api.getToilet(id);
            if(t == null){
                plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("not_exits_toilet")));
                return;
            }
            t.setDoor(l.getBlock());
            plugin.api.saveToilet(t);
            plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("done")));
        } else {
            plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("door_wrong_material")));
        }
    }

    @Subcommand("toilet destroy")
    @Description("Remove a toilet")
    @CommandPermission("at.toilet.destroy")
    public void destroy(CommandSender sender, int id){
        plugin.api.destroyToilet(id);
    }

    @Subcommand("toilet tp")
    @Description("Teleport to a toilet")
    @CommandPermission("at.toilet.tp")
    public void tp(Player player, int id){
        Toilet t = plugin.api.getToilet(id);
        if(t == null){
            plugin.chat.message(player, Objects.requireNonNull(plugin.messageConf.getString("not_exits_toilet")));
            return;
        }
        player.teleport(t.getSpawnPoint());
    }

    @Subcommand("payshit")
    @Description("Tặng cục shit cho ai đó")
    @CommandPermission("at.payshit")
    public void give(CommandSender sender, Player another){
        another.getInventory().addItem(plugin.api.getShit());
        plugin.chat.message(another, Objects.requireNonNull(plugin.messageConf.getString("shit_block_holding")));
        plugin.chat.message(sender, Objects.requireNonNull(plugin.messageConf.getString("shit_block_given")));
    }

    @Subcommand("force pee")
    @Description("Bắt ai đó tiểu tiện")
    @CommandPermission("at.force.pee")
    public void doPee(CommandSender sender, int id, Player another){
        Toilet t = plugin.api.getToilet(id);
        if(t == null){
            plugin.chat.message(sender, Objects.requireNonNull(plugin.messageConf.getString("not_exits_toilet")));
            return;
        }
        plugin.toiletHandler.newActive(another, t, ToiletActivity.PEE);
        plugin.chat.message(sender, Objects.requireNonNull(plugin.messageConf.getString("done")));
    }

    @Subcommand("force poop")
    @Description("Bắt ai đó đại tiện")
    @CommandPermission("at.force.poop")
    public void doPoop(CommandSender sender, int id, Player another){
        Toilet t = plugin.api.getToilet(id);
        if(t == null){
            plugin.chat.message(sender, Objects.requireNonNull(plugin.messageConf.getString("not_exits_toilet")));
            return;
        }
        plugin.toiletHandler.newActive(another, t, ToiletActivity.POOP);
        plugin.chat.message(sender, Objects.requireNonNull(plugin.messageConf.getString("done")));
    }
}

