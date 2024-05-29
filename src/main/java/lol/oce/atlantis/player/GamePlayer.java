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
// TODO: 2 prevent future code chaos and excessive redundancy, i think its time 2 schedule refactoring session
public class GamePlayer {
    UUID uuid;
    Player player;
    PersistencePlayerData persistencePlayerData;
    MatchPlayerData matchPlayerData;

    public static GamePlayer createDefault(Player player) {
        Atlantis atlantis = Atlantis.getInstance();
        Config dataConfig = atlantis.getDataConfig();

        PlayerManager playerManager = PlayerManager.getInstance();

        String playerName = player.getName();
        UUID playerUniqueId = player.getUniqueId();
        String playerUniqueIdString = playerUniqueId.toString();

        GamePlayer gamePlayer = new GamePlayer(
                playerUniqueId, player, PersistencePlayerData.createDefault(), new MatchPlayerData(0, 0)
        );

        playerManager.addPlayer(gamePlayer);

        String dataConfigPrefix = "players." + playerUniqueId;

        // TODO: we can insert data into mongo but save method only saves file and doesnt interact with mongodb
        dataConfig.set(dataConfigPrefix + ".name", playerName);
        dataConfig.set(dataConfigPrefix + ".kills", 0);
        dataConfig.set(dataConfigPrefix + ".deaths", 0);
        dataConfig.set(dataConfigPrefix + ".wins", 0);
        dataConfig.set(dataConfigPrefix + ".games", 0);
        dataConfig.set(dataConfigPrefix + ".level", 1);
        dataConfig.set(dataConfigPrefix + ".xp", 0);
        dataConfig.set(dataConfigPrefix + ".coins", 0);

        atlantis.saveConfig();

        playerManager.addPlayer(gamePlayer);

        if (atlantis.getStorage() == Atlantis.Storage.MONGO) {
            Document document = new Document();
            MongoManager mongoManager = MongoManager.getInstance();

            document.append("uuid", playerUniqueIdString);
            document.append("name", playerName);
            document.append("kills", 0);
            document.append("deaths", 0);
            document.append("wins", 0);
            document.append("games", 0);
            document.append("level", 1);
            document.append("xp", 0);
            document.append("coins", 0);

            mongoManager.getCollection().insertOne(document);
        }

        return gamePlayer;
    }

    public static GamePlayer create(UUID uuid, Player player) {
        Atlantis atlantis = Atlantis.getInstance();
        PlayerManager playerManager = PlayerManager.getInstance();

        PersistencePlayerData persistencePlayerData;

        String uuidString = uuid.toString();
        int kills, deaths, wins, games, levels, xp, coins;

        if (atlantis.getStorage() == Atlantis.Storage.MONGO) {
            MongoManager mongoManager = MongoManager.getInstance();
            MongoCollection<Document> collection = mongoManager.getCollection();

            // TODO: null check
            kills = collection.find(new Document("uuid", uuidString)).first().getInteger("kills");
            deaths = collection.find(new Document("uuid", uuidString)).first().getInteger("deaths");
            wins = collection.find(new Document("uuid", uuidString)).first().getInteger("wins");
            games = collection.find(new Document("uuid", uuidString)).first().getInteger("games");
            levels = collection.find(new Document("uuid", uuidString)).first().getInteger("level");
            xp = collection.find(new Document("uuid", uuidString)).first().getInteger("xp");
            coins = collection.find(new Document("uuid", uuidString)).first().getInteger("coins");

            Document document = new Document();

            document.append("uuid", uuid);
            document.append("name", player.getName());
            document.append("kills", kills);
            document.append("deaths", deaths);
            document.append("wins", wins);
            document.append("games", games);

            persistencePlayerData = new PersistencePlayerData(kills, deaths, wins, games, levels, xp, coins);

            QuickUtils.info("Player data loaded from MongoDB");
            QuickUtils.info("Kills: " + kills + " Deaths: " + deaths + " Wins: " + wins + " Games: " + games + "Level: " + levels + " XP: " + xp + " Coins: " + coins);
        } else {
            persistencePlayerData = PersistencePlayerData.createDefault();
            QuickUtils.info("New player detected, creating default data");
        }

        playerManager.addPlayer(new GamePlayer(uuid, player, persistencePlayerData, new MatchPlayerData(0, 0)));
        return new GamePlayer(uuid, player, persistencePlayerData, new MatchPlayerData(0, 0));
    }

    public GamePlayer save() {
        Document document = new Document();

        document.append("uuid", player.getUniqueId().toString());
        document.append("name", player.getName());
        document.append("kills", persistencePlayerData.getKills());
        document.append("deaths", persistencePlayerData.getDeaths());
        document.append("wins", persistencePlayerData.getWins());
        document.append("games", persistencePlayerData.getGames());
        document.append("level", persistencePlayerData.getLevel());
        document.append("xp", persistencePlayerData.getXp());
        document.append("coins", persistencePlayerData.getCoins());

        MongoManager.getInstance().getCollection().insertOne(document);
        return this;
    }
}
