package lol.oce.atlantis;

import de.leonhard.storage.Config;
import de.leonhard.storage.SimplixBuilder;
import de.leonhard.storage.internal.settings.DataType;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import lol.oce.atlantis.commands.MatchJoinCommand;
import lol.oce.atlantis.commands.MatchStartCommand;
import lol.oce.atlantis.database.MongoManager;
import lol.oce.atlantis.listeners.PlayerListener;
import lol.oce.atlantis.scoreboard.BoardManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Atlantis extends JavaPlugin {

    @Getter
    private static Atlantis instance;
    @Getter
    Storage storage = Storage.FILE;
    @Getter
    Config mainConfig, messagesConfig, kitsConfig, scoreboardsConfig, dataConfig;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Atlantic enabled!");

        // Commands
        this.liteCommands = LiteCommandsBukkit.builder()
                .commands(
                        new MatchJoinCommand(),
                        new MatchStartCommand()
                )
                .build();

        // Configs
        mainConfig = SimplixBuilder
                .fromFile(new File(getDataFolder(), "config.yml"))
                .setName("config")
                .addInputStreamFromResource("config.yml")
                .setDataType(DataType.SORTED)
                .createConfig().addDefaultsFromInputStream();
        messagesConfig = SimplixBuilder
                .fromFile(new File(getDataFolder(), "messages.yml"))
                .setName("messages")
                .addInputStreamFromResource("messages.yml")
                .setDataType(DataType.SORTED)
                .createConfig().addDefaultsFromInputStream();
        kitsConfig = SimplixBuilder
                .fromFile(new File(getDataFolder(), "kits.yml"))
                .setName("kits")
                .addInputStreamFromResource("kits.yml")
                .setDataType(DataType.SORTED)
                .createConfig().addDefaultsFromInputStream();
        scoreboardsConfig = SimplixBuilder
                .fromFile(new File(getDataFolder(), "scoreboards.yml"))
                .setName("scoreboards")
                .addInputStreamFromResource("scoreboards.yml")
                .setDataType(DataType.SORTED)
                .createConfig().addDefaultsFromInputStream();
        dataConfig = SimplixBuilder
                .fromFile(new File(getDataFolder(), "data.yml"))
                .setName("data")
                .addInputStreamFromResource("data.yml")
                .setDataType(DataType.SORTED)
                .createConfig().addDefaultsFromInputStream();

        if (mainConfig.getString("storage").contains("MONGO")) {
            MongoManager.getInstance().load(mainConfig.getString("mongodb.uri"),
                    mainConfig.getString("mongodb.database"));
            storage = Storage.MONGO;
        } else {
            storage = Storage.FILE;

        }

        // Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Scoreboards
        BoardManager.getInstance().updateAll();
    }

    @Override
    public void onDisable() {
        getLogger().info("Atlantic disabled!");
        liteCommands.unregister();
    }

    @Getter
    public enum Storage {
        FILE, MONGO
    }

}