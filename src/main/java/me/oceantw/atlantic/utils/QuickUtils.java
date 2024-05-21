package me.oceantw.atlantic.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@UtilityClass
public class QuickUtils {

    public static void info(String message) {
        log(ChatColor.GREEN + "[INFO] " + message);
    }

    public static void warn(String message) {
        log(ChatColor.YELLOW + "[WARN] " + message);
    }

    public static void error(String message) {
        log(ChatColor.RED + "[ERROR] " + message);
    }

    public static void debug(String message) {
        log(ChatColor.GRAY + "[DEBUG] " + message);
    }

    public static void log(String message) {
        Bukkit.getLogger().info(StringUtils.handleString(message));
    }
}
