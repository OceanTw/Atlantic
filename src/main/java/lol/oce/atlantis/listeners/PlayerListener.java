package lol.oce.atlantis.listeners;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PersistencePlayerData;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.scoreboard.BoardManager;
import lol.oce.atlantis.types.PlayerStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUniqueId = player.getUniqueId();

        Atlantis atlantis = Atlantis.getInstance();
        Atlantis.Storage storage = atlantis.getStorage();

        BoardManager boardManager = BoardManager.getInstance();
        PlayerManager playerManager = PlayerManager.getInstance();

        if (storage == Atlantis.Storage.MONGO) {
            GamePlayer gamePlayer = playerManager.isPlayerExists(player) ?
                    GamePlayer.create(playerUniqueId, player) : GamePlayer.createDefault(player);

            playerManager.setPlayerStatus(gamePlayer, PlayerStatus.LOBBY);
            boardManager.update(gamePlayer);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerManager.getInstance().removePlayer(
                PlayerManager.getInstance().getGamePlayer(event.getPlayer())
        );
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        PlayerManager playerManager = PlayerManager.getInstance();

        Optional<Player> killerOptional = Optional.ofNullable(player.getKiller());

        // 只有击杀者也为玩家时才增加次数
        killerOptional.ifPresent(killer -> {
            GamePlayer gamePlayer = playerManager.getGamePlayer(player);
            PersistencePlayerData playerPersistencePlayerData = gamePlayer.getPersistencePlayerData();
            playerPersistencePlayerData.setDeaths(playerPersistencePlayerData.getDeaths() + 1);

            GamePlayer killerGamePlayer = playerManager.getGamePlayer(killer);
            PersistencePlayerData killerPersistencePlayerData = killerGamePlayer.getPersistencePlayerData();

            killerPersistencePlayerData
                    .setKills(killerPersistencePlayerData.getKills() + 1);

            gamePlayer.save();
            killerGamePlayer.save();
        });
    }
}
