package lol.oce.atlantis.listeners;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.database.MongoManager;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    // TODO: Finish this thing
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Atlantis.getInstance().getStorage() == Atlantis.Storage.MONGO) {

        }
    }


}
