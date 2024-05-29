package lol.oce.atlantis.scoreboard;

import de.leonhard.storage.Config;
import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.match.Match;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PersistencePlayerData;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.types.PlayerStatus;
import lol.oce.atlantis.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class BoardManager {

    @Getter
    private static final BoardManager instance = new BoardManager();

    public Board lobby;
    public Board game;

    public void update(GamePlayer gamePlayer) {
        Atlantis atlantis = Atlantis.getInstance();
        Config scoreboardsConfig = atlantis.getScoreboardsConfig();

        PlayerManager playerManager = PlayerManager.getInstance();
        PlayerStatus status = playerManager.getPlayerStatus(gamePlayer);

        Player player = gamePlayer.getPlayer();
        PersistencePlayerData persistencePlayerData = gamePlayer.getPersistencePlayerData();

        if (status == PlayerStatus.LOBBY) {
            lobby = new Board(scoreboardsConfig.getString("scoreboards.lobby.title"));
            List<String> lines = scoreboardsConfig.getStringList("scoreboards.lobby.lines");

            for (int i = 0; i < lines.size(); i++) {
                lobby.setLine(
                        i,
                        StringUtils.handleString(
                                lines.get(i),

                                "{kills}",
                                Integer.toString(persistencePlayerData.getKills()),

                                "{wins}",
                                Integer.toString(persistencePlayerData.getWins()),

                                "{coins}",
                                Integer.toString(persistencePlayerData.getCoins()),

                                "{level}",
                                Integer.toString(persistencePlayerData.getLevel()),

                                "{xp}",
                                Integer.toString(persistencePlayerData.getXp())
                        )
                );
            }

            lobby.show(player);
        }

        if (status == PlayerStatus.PLAYING) {
            Match playerMatch = playerManager.getPlayerMatch(gamePlayer);

            game = new Board(scoreboardsConfig.getString("scoreboards.game.title"));
            List<String> lines = scoreboardsConfig.getStringList("scoreboards.game.lines");

            for (int i = 0; i < lines.size(); i++) {
                game.setLine(
                        i,
                        StringUtils.handleString(
                                lines.get(i),

                                "{stage}",
                                playerMatch.getStage().name(),

                                "{time}",
                                Integer.toString(playerMatch.getNextStageTime()),

                                "{players}",
                                Integer.toString(playerMatch.getPlayers().size()),

                                "{kills}",
                                Integer.toString(persistencePlayerData.getKills()),

                                "{damage}",
                                Integer.toString(gamePlayer.getMatchPlayerData().getDamageDealt())
                        )
                );
            }


            game.show(player);
        }
    }

    public void updateAll() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> update(PlayerManager.getInstance().getGamePlayer(player)));
    }
}
