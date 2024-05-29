package lol.oce.atlantis.scoreboard;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.types.PlayerStatus;
import lol.oce.atlantis.utils.QuickUtils;
import lol.oce.atlantis.utils.StringUtils;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class BoardManager {

    Board lobby;
    Board game;

    @Getter
    private static BoardManager instance = new BoardManager();

    public void update(GamePlayer player) {
        PlayerStatus status = PlayerManager.getInstance().getPlayerStatus(player);

        if (status == PlayerStatus.LOBBY) {
            lobby = new Board(Atlantis.getInstance().getScoreboardsConfig().getString("scoreboards.lobby.title"));
            List<String> lines = Atlantis.getInstance().getScoreboardsConfig().getStringList("scoreboards.lobby.lines");

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                line = StringUtils.handleString(line,
                        "{kills}", Integer.toString(player.getPersistencePlayerData().getKills()),
                        "{wins}", Integer.toString(player.getPersistencePlayerData().getWins()),
                        "{coins}", Integer.toString(player.getPersistencePlayerData().getCoins()),
                        "{level}", Integer.toString(player.getPersistencePlayerData().getLevel()),
                        "{xp}", Integer.toString(player.getPersistencePlayerData().getXp())
                );
                lobby.setLine(i, line);
            }


            lobby.show(player.getPlayer());
        }

        if (status == PlayerStatus.PLAYING) {
            game = new Board(Atlantis.getInstance().getScoreboardsConfig().getString("scoreboards.game.title"));
            List<String> lines = Atlantis.getInstance().getScoreboardsConfig().getStringList("scoreboards.game.lines");

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                line = StringUtils.handleString(line,
                        "{stage}", PlayerManager.getInstance()
                                .getPlayerMatch(player
                                ).getStage().name(),
                        "{time}", Integer.toString(PlayerManager.getInstance().getPlayerMatch(player)
                                .getNextStageTime()),
                        "{players}", Integer.toString(PlayerManager.getInstance().getPlayerMatch(player).getPlayers().size()),
                        "{kills}", Integer.toString(player.getPersistencePlayerData().getKills()),
                        "{damage}", Integer.toString(player.getMatchPlayerData().getDamageDealt()));
                game.setLine(i, line);
            }


            game.show(player.getPlayer());
        }
    }

    public void updateAll() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            update(PlayerManager.getInstance().getGamePlayer(player));
        }
    }

}
