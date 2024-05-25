package lol.oce.atlantis.listeners;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Atlantis.getInstance().getStorage() == Atlantis.Storage.MONGO) {
            if (!PlayerManager.getInstance().isPlayerExists(player)) {
                GamePlayer.createDefault(player);
            } else {
                GamePlayer.create(player.getUniqueId(), player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(player);
        PlayerManager.getInstance().removePlayer(gamePlayer);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(player);
        gamePlayer.getPersistencePlayerData().setDeaths(gamePlayer.getPersistencePlayerData().getDeaths() + 1);
        double damage = player.getLastDamageCause().getDamage();
        Player killer = player.getKiller();
        if (killer != null) {
            GamePlayer killerGamePlayer = PlayerManager.getInstance().getGamePlayer(killer);
            killerGamePlayer.getPersistencePlayerData().setKills(killerGamePlayer.getPersistencePlayerData().getKills() + 1);
        }
    }


}
