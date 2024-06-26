package lol.oce.atlantis;

import de.leonhard.storage.Config;
import de.leonhard.storage.SimplixBuilder;
import de.leonhard.storage.internal.settings.DataType;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import lol.oce.atlantis.commands.MatchJoinCommand;
import lol.oce.atlantis.commands.MatchStartCommand;
import lol.oce.atlantis.commands.StatsUpdateCommand;
import lol.oce.atlantis.commands.StatusShowCommand;
import lol.oce.atlantis.database.MongoManager;
import lol.oce.atlantis.player.PlayerListener;
import lol.oce.atlantis.scoreboards.ScoreboardAdapter;
import lol.oce.atlantis.scoreboards.scoreboards.Assemble;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Atlantis extends JavaPlugin {

    @Getter
    private static Atlantis instance;
    @Getter
    private Storage storage = Storage.FILE;
    @Getter
    private Config mainConfig, messagesConfig, kitsConfig, scoreboardsConfig, dataConfig;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Atlantic enabled!");

        registerCommands();
        registerListeners();
        loadConfigs();
        loadScoreboards();
        configureStorage();
    }

    @Override
    public void onDisable() {
        getLogger().info("Atlantic disabled!");
        liteCommands.unregister();
    }

    private void registerCommands() {
        liteCommands = LiteCommandsBukkit.builder()
                .commands(
                        new MatchJoinCommand(),
                        new MatchStartCommand(),
                        new StatsUpdateCommand(),
                        new StatusShowCommand()
                )
                .build();
    }

    private void loadConfigs() {
        mainConfig = loadConfig("config.yml");
        messagesConfig = loadConfig("messages.yml");
        kitsConfig = loadConfig("kits.yml");
        scoreboardsConfig = loadConfig("scoreboards.yml");
        dataConfig = loadConfig("data.yml");
    }

    private Config loadConfig(String fileName) {

        return SimplixBuilder
                .fromFile(new File(getDataFolder(), fileName))
                .setName(fileName.replace(".yml", ""))
                .addInputStreamFromResource(fileName)
                .setDataType(DataType.SORTED)
                .createConfig().addDefaultsFromInputStream();
    }

    private void loadScoreboards() {
        Assemble assemble = new Assemble(this, new ScoreboardAdapter());
        assemble.setTicks(2);
    }

    private void configureStorage() {
        if (mainConfig.getString("storage").contains("MONGO")) {
            MongoManager.getInstance().load(mainConfig.getString("mongodb.uri"),
                    mainConfig.getString("mongodb.database"));
            storage = Storage.MONGO;
        } else {
            storage = Storage.FILE;
        }
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Getter
    public enum Storage {
        FILE, MONGO
    }
}
