package lol.oce.atlantis.player;

import com.mongodb.client.MongoCollection;
import de.leonhard.storage.Config;
import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.database.MongoManager;
import lol.oce.atlantis.utils.QuickUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class GamePlayer {
    UUID uuid;
    Player player;
    PersistencePlayerData persistencePlayerData;
    MatchPlayerData matchPlayerData;

    public static GamePlayer createDefault(Player player) {
        Atlantis atlantis = Atlantis.getInstance();
        Config dataConfig = atlantis.getDataConfig();
        PlayerManager playerManager = PlayerManager.getInstance();

        UUID playerUniqueId = player.getUniqueId();
        String dataConfigPrefix = "players." + playerUniqueId;

        GamePlayer gamePlayer = new GamePlayer(
                playerUniqueId, player, PersistencePlayerData.createDefault(), new MatchPlayerData(0, 0)
        );

        saveToConfig(dataConfig, dataConfigPrefix, player);
        atlantis.saveConfig();

        playerManager.addPlayer(gamePlayer);

        if (atlantis.getStorage() == Atlantis.Storage.MONGO) {
            saveToMongo(player);
        }

        return gamePlayer;
    }

    public static GamePlayer create(UUID uuid, Player player) {
        Atlantis atlantis = Atlantis.getInstance();
        PlayerManager playerManager = PlayerManager.getInstance();

        PersistencePlayerData persistencePlayerData = atlantis.getStorage() == Atlantis.Storage.MONGO
                ? loadFromMongo(uuid)
                : PersistencePlayerData.createDefault();

        playerManager.addPlayer(new GamePlayer(uuid, player, persistencePlayerData, new MatchPlayerData(0, 0)));
        return new GamePlayer(uuid, player, persistencePlayerData, new MatchPlayerData(0, 0));
    }

    public GamePlayer save() {
        saveToMongo(player);
        return this;
    }

    private static void saveToConfig(Config config, String prefix, Player player) {
        config.set(prefix + ".name", player.getName());
        config.set(prefix + ".kills", 0);
        config.set(prefix + ".deaths", 0);
        config.set(prefix + ".wins", 0);
        config.set(prefix + ".games", 0);
        config.set(prefix + ".level", 1);
        config.set(prefix + ".xp", 0);
        config.set(prefix + ".coins", 0);
    }

    private static void saveToMongo(Player player) {
        MongoManager mongoManager = MongoManager.getInstance();
        Document document = new Document();
        UUID playerUniqueId = player.getUniqueId();

        document.append("uuid", playerUniqueId.toString())
                .append("name", player.getName())
                .append("kills", 0)
                .append("deaths", 0)
                .append("wins", 0)
                .append("games", 0)
                .append("level", 1)
                .append("xp", 0)
                .append("coins", 0);

        mongoManager.getCollection().insertOne(document);
    }

    private static PersistencePlayerData loadFromMongo(UUID uuid) {
        MongoManager mongoManager = MongoManager.getInstance();
        MongoCollection<Document> collection = mongoManager.getCollection();
        Document document = collection.find(new Document("uuid", uuid.toString())).first();

        if (document != null) {
            int kills = document.getInteger("kills");
            int deaths = document.getInteger("deaths");
            int wins = document.getInteger("wins");
            int games = document.getInteger("games");
            int level = document.getInteger("level");
            int xp = document.getInteger("xp");
            int coins = document.getInteger("coins");

            QuickUtils.info(String.format("Player data loaded from MongoDB: Kills: %d, Deaths: %d, Wins: %d, Games: %d, Level: %d, XP: %d, Coins: %d",
                    kills, deaths, wins, games, level, xp, coins));

            return new PersistencePlayerData(kills, deaths, wins, games, level, xp, coins);
        }

        QuickUtils.info("New player detected, creating default data");
        return PersistencePlayerData.createDefault();
    }
}
