package me.oceantw.atlantic;

import de.leonhard.storage.Config;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Atlantic extends JavaPlugin {

    @Getter
    private static Atlantic instance;

    @Getter
    Config MainConfig, messagesConfig, kitsConfig, scoreboardsConfig, dataConfig;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Atlantic enabled!");

        MainConfig = new Config("config", getDataFolder().getPath());
        messagesConfig = new Config("messages", getDataFolder().getPath());
        kitsConfig = new Config("kits", getDataFolder().getPath());
        scoreboardsConfig = new Config("scoreboards", getDataFolder().getPath());

    }

    @Override
    public void onDisable() {
        getLogger().info("Atlantic disabled!");
    }

}