package me.oceantw.atlantic.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
@Data
public class GamePlayer {
    UUID uuid;
    Player player;
    PersistencePlayerData persistencePlayerData;

    public static GamePlayer createDefault(Player player) {
        return new GamePlayer(player.getUniqueId(), player, PersistencePlayerData.createDefault());
    }

    public static GamePlayer create(UUID uuid, Player player, PersistencePlayerData persistencePlayerData) {
        return new GamePlayer(uuid, player, persistencePlayerData);
    }
}
