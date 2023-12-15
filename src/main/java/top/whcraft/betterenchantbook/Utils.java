package top.whcraft.betterenchantbook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {
    public static void Message(CommandSender p, String s) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }
}
