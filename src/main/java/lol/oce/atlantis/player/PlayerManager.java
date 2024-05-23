package lol.oce.atlantis.player;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.database.MongoManager;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerManager {

    @Getter
    List<GamePlayer> players;

    @Getter
    private static PlayerManager instance;

    public GamePlayer getGamePlayer(Player player) {
        return players.stream()
                .filter(gamePlayer -> gamePlayer.getPlayer().equals(player))
                .findFirst()
                .orElse(null);
    }

    public boolean isPlayerExists(Player player) {
        if (Atlantis.getInstance().getStorage() == Atlantis.Storage.MONGO) {
            Document document = new Document();
            document = new Document("uuid", player.getUniqueId().toString());
            return MongoManager.getInstance().getCollection().find(document).first().isEmpty();
        } else {
            return false;
        }
    }

    public void addPlayer(GamePlayer gamePlayer) {
        players.add(gamePlayer);
    }
}
