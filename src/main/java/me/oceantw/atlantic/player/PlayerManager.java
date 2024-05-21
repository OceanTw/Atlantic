package me.oceantw.atlantic.player;

import lombok.Data;
import lombok.Getter;
import me.oceantw.atlantic.Atlantic;
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
        return Atlantic.getInstance().getDataConfig().contains("players." + player.getUniqueId());
    }

    public void addPlayer(GamePlayer gamePlayer) {
        players.add(gamePlayer);
    }
}
