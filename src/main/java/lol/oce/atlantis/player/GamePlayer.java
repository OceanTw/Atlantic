package lol.oce.atlantis.player;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.database.MongoManager;
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

    public static GamePlayer createDefault(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player.getUniqueId(), player, PersistencePlayerData.createDefault());
        PlayerManager.getInstance().getPlayers().add(gamePlayer);

        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".name", player.getName());
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".kills", 0);
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".deaths", 0);
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".wins", 0);
        Atlantis.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".games", 0);
        Atlantis.getInstance().saveConfig();
        PlayerManager.getInstance().addPlayer(gamePlayer);

        if (Atlantis.getInstance().getStorage() == Atlantis.Storage.MONGO) {
            Document document = new Document();
            document.append("uuid", player.getUniqueId());
            document.append("name", player.getName());
            document.append("kills", 0);
            document.append("deaths", 0);
            document.append("wins", 0);
            document.append("games", 0);
            MongoManager.getInstance().getCollection().insertOne(document);
        }
        return gamePlayer;
    }

    public static GamePlayer create(UUID uuid, Player player) {
        PersistencePlayerData persistencePlayerData;
        if (Atlantis.getInstance().getStorage() == Atlantis.Storage.MONGO) {
            int kills, deaths, wins, games;
            kills = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid)).first()
                    .getInteger("kills");
            deaths = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid)).first()
                    .getInteger("deaths");
            wins = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid)).first()
                    .getInteger("wins");
            games = MongoManager.getInstance().getCollection().find(new Document("uuid", uuid)).first()
                    .getInteger("games");

            Document document = new Document();
            document.append("uuid", uuid);
            document.append("name", player.getName());
            document.append("kills", kills);
            document.append("deaths", deaths);
            document.append("wins", wins);
            document.append("games", games);
            persistencePlayerData = new PersistencePlayerData(kills, deaths, wins, games);
        } else {
            persistencePlayerData = PersistencePlayerData.createDefault();
        }

        PlayerManager.getInstance().addPlayer(new GamePlayer(uuid, player, persistencePlayerData));
        return new GamePlayer(uuid, player, persistencePlayerData);
    }
}