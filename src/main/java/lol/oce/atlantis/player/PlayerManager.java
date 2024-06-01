package lol.oce.atlantis.player;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.database.MongoManager;
import lol.oce.atlantis.match.Match;
import lol.oce.atlantis.types.PlayerStatus;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerManager {

    @Getter
    private static final PlayerManager instance = new PlayerManager();
    @Getter
    List<GamePlayer> players = new ArrayList<>();
    HashMap<GamePlayer, PlayerStatus> playerStatus = new HashMap<>();
    HashMap<GamePlayer, Match> playerMatch = new HashMap<>();

    public GamePlayer getGamePlayer(Player player) {
        return players.stream()
                .filter(gamePlayer -> gamePlayer.getPlayer().equals(player))
                .findFirst()
                .orElse(null);
    }

    public boolean isPlayerExists(Player player) {
        if (Atlantis.getInstance().getStorage() == Atlantis.Storage.MONGO) {
            Document document = new Document("uuid", player.getUniqueId().toString());
            return !(MongoManager.getInstance().getCollection().find(document).first() == null);
        } else {
            return false;
        }
    }

    public void addPlayer(GamePlayer gamePlayer) {
        players.add(gamePlayer);
    }

    public void removePlayer(GamePlayer gamePlayer) {
        players.remove(gamePlayer);
    }

    public void setPlayerStatus(GamePlayer gamePlayer, PlayerStatus status) {
        if (playerStatus.containsKey(gamePlayer)) {
            playerStatus.replace(gamePlayer, status);
        } else {
            playerStatus.put(gamePlayer, status);
        }
    }

    public void setPlayerMatch(GamePlayer gamePlayer, Match match) {
        if (playerMatch.containsKey(gamePlayer)) {
            playerMatch.replace(gamePlayer, match);
        } else {
            playerMatch.put(gamePlayer, match);
        }
    }

    public Match getPlayerMatch(GamePlayer gamePlayer) {
        return playerMatch.get(gamePlayer);
    }

    public PlayerStatus getPlayerStatus(GamePlayer gamePlayer) {
        return playerStatus.get(gamePlayer);
    }


}
