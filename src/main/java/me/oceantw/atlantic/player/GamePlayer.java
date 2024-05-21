package me.oceantw.atlantic.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.oceantw.atlantic.Atlantic;
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

        Atlantic.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".name", player.getName());
        Atlantic.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".kills", 0);
        Atlantic.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".deaths", 0);
        Atlantic.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".wins", 0);
        Atlantic.getInstance().getDataConfig().set("players." + player.getUniqueId() + ".games", 0);
        Atlantic.getInstance().saveConfig();
        PlayerManager.getInstance().addPlayer(gamePlayer);
        return gamePlayer;
    }

    public static GamePlayer create(UUID uuid, Player player, PersistencePlayerData persistencePlayerData) {
        PlayerManager.getInstance().addPlayer(new GamePlayer(uuid, player, persistencePlayerData));
        return new GamePlayer(uuid, player, persistencePlayerData);
    }
}
