package lol.oce.atlantis;

import com.mongodb.client.MongoClient;
import de.leonhard.storage.Config;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import lol.oce.atlantis.commands.MatchJoinCommand;
import lol.oce.atlantis.commands.MatchStartCommand;
import lol.oce.atlantis.database.MongoManager;
import lol.oce.atlantis.listeners.PlayerListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Atlantis extends JavaPlugin {

    @Getter
    private static Atlantis instance;

    @Getter
    public enum Storage {
        FILE, MONGO
    }

    @Getter
    Storage storage = Storage.FILE;

    @Getter
    Config MainConfig, messagesConfig, kitsConfig, scoreboardsConfig, dataConfig;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Atlantic enabled!");

        this.liteCommands = LiteCommandsBukkit.builder()
                .commands(
                        new MatchJoinCommand(),
                        new MatchStartCommand()
                )
                .build();

        MainConfig = new Config("config", getDataFolder().getPath());
        messagesConfig = new Config("messages", getDataFolder().getPath());
        kitsConfig = new Config("kits", getDataFolder().getPath());
        scoreboardsConfig = new Config("scoreboards", getDataFolder().getPath());
        dataConfig = new Config("data", getDataFolder().getPath());

        if (MainConfig.get("storage").toString().contains("MONGO")) {
            MongoManager.getInstance().load(MainConfig.getString("mongodb.uri"),
                    MainConfig.getString("mongodb.database"));
            storage = Storage.MONGO;
        }


        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Atlantic disabled!");
        liteCommands.unregister();
        MongoManager.getInstance().close();
    }

}