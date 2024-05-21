package me.oceantw.atlantic.player;

import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerManager {

    List<GamePlayer> players;

    @Getter
    private static PlayerManager instance;

    // TODO: Get the player from the database
    public GamePlayer getGamePlayer(Player player) {
        return new GamePlayer(player.getUniqueId(), player, PersistencePlayerData.createDefault());
    }
}
