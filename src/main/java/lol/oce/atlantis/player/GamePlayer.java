package lol.oce.atlantis.player;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.database.MongoManager;
import lol.oce.atlantis.utils.QuickUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
@Data
public class GamePlayer {
    UUID uuid;
    Player player;
    PersistencePlayerData persistencePlayerData;
    MatchPlayerData matchPlayerData;

    public static GamePlayer createDefault(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player.getUniqueId(), player, PersistencePlayerData.createDefault(), new MatchPlayerData(0, 0));
        PlayerManager.getInstance().addPlayer(gamePlayer);

        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".name", player.getName());
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".kills", 0);
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".deaths", 0);
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".wins", 0);
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".games", 0);
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".level", 1);
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".xp", 0);
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".coins", 0);
        Atlantis.getInstance().saveConfig();
        PlayerManager.getInstance().addPlayer(gamePlayer);

        if (Atlantis.getInstance().getStorage() == Atlantis.Storage.MONGO) {
            Document document = new Document();
            document.append("uuid", player.getUniqueId().toString());
            document.append("name", player.getName());
            document.append("kills", 0);
            document.append("deaths", 0);
            document.append("wins", 0);
            document.append("games", 0);
            document.append("level", 1);
            document.append("xp", 0);
            document.append("coins", 0);
            MongoManager.getInstance().getCollection().insertOne(document);
        }
        return gamePlayer;
    }

    public static GamePlayer create(UUID uuid, Player player) {
        PersistencePlayerData persistencePlayerData;
        if (Atlantis.getInstance().getStorage() == Atlantis.Storage.MONGO) {
            int kills, deaths, wins, games, levels, xp, coins;
            kills = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid.toString())).first()
                    .getInteger("kills");
            deaths = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid.toString())).first()
                    .getInteger("deaths");
            wins = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid.toString())).first()
                    .getInteger("wins");
            games = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid.toString())).first()
                    .getInteger("games");
            levels = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid.toString())).first()
                    .getInteger("level");
            xp = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid.toString())).first()
                    .getInteger("xp");
            coins = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid.toString())).first()
                    .getInteger("coins");

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

        PlayerManager.getInstance().addPlayer(new GamePlayer(uuid, player, persistencePlayerData, new MatchPlayerData(0, 0)));
        return new GamePlayer(uuid, player, persistencePlayerData, new MatchPlayerData(0, 0));
    }
}
