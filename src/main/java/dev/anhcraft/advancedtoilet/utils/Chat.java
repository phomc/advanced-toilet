package dev.anhcraft.advancedtoilet.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Chat {
    private final String prefix;

    public Chat(String prefix) {
        this.prefix = prefix;
    }

    public void message(CommandSender sender, String s) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + s));
    }
}
