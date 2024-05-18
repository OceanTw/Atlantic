package me.oceantw.atlantic;

import org.bukkit.plugin.java.JavaPlugin;

public class Atlantic extends JavaPlugin {


    @Override
    public void onEnable() {
        getLogger().info("Atlantic enabled!");

    }

    @Override
    public void onDisable() {
        getLogger().info("Atlantic disabled!");
    }
}