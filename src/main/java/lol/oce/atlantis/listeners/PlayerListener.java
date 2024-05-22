package lol.oce.atlantis.listeners;

import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (PlayerManager.getInstance().getGamePlayer(player) == null) {
            GamePlayer.createDefault(player);
        }
    }
}
