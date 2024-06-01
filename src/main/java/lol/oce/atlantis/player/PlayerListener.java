package lol.oce.atlantis.player;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.scoreboards.ScoreboardAdapter;
import lol.oce.atlantis.types.PlayerStatus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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

        PlayerManager playerManager = PlayerManager.getInstance();

        if (storage == Atlantis.Storage.MONGO) {
            GamePlayer gamePlayer = playerManager.isPlayerExists(player) ?
                    GamePlayer.create(playerUniqueId, player) : GamePlayer.createDefault(player);

            playerManager.setPlayerStatus(gamePlayer, PlayerStatus.LOBBY);
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

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        Optional<Entity> damagerOptional = Optional.of(event.getEntity());

        damagerOptional.ifPresent(player -> {
            if (player instanceof Player) {
                if (PlayerManager.getInstance().getPlayerMatch(PlayerManager.getInstance().getGamePlayer((Player) player)) == null) {
                    event.setCancelled(true);
                }
                PlayerManager.getInstance().getGamePlayer(((Player) player).getPlayer()).getMatchPlayerData().setDamageDealt(event.getDamage());
            }
        });
    }
}
