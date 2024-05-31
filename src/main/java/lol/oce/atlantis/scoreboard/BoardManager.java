package lol.oce.atlantis.scoreboard;

import de.leonhard.storage.Config;
import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.match.Match;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PersistencePlayerData;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.types.PlayerStatus;
import lol.oce.atlantis.utils.QuickUtils;
import lol.oce.atlantis.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class BoardManager {

    @Getter
    private static final BoardManager instance = new BoardManager();

    private Board lobby;
    private Board game;

    public void update(GamePlayer gamePlayer) {
        Atlantis atlantis = Atlantis.getInstance();
        Config scoreboardsConfig = atlantis.getScoreboardsConfig();

        PlayerManager playerManager = PlayerManager.getInstance();
        PlayerStatus status = playerManager.getPlayerStatus(gamePlayer);

        Player player = gamePlayer.getPlayer();
        PersistencePlayerData persistencePlayerData = gamePlayer.getPersistencePlayerData();

        switch (status) {
            case LOBBY -> {
                lobby = new Board(scoreboardsConfig.getString("scoreboards.lobby.title"));
                List<String> lines = scoreboardsConfig.getStringList("scoreboards.lobby.lines");

                for (int i = 0; i < lines.size(); i++) {
                    String line = StringUtils.handleString(lines.get(i),
                            "{kills}", Integer.toString(persistencePlayerData.getKills()),
                            "{wins}", Integer.toString(persistencePlayerData.getWins()),
                            "{coins}", Integer.toString(persistencePlayerData.getCoins()),
                            "{level}", Integer.toString(persistencePlayerData.getLevel()),
                            "{xp}", Integer.toString(persistencePlayerData.getXp()));

                    lobby.setLine(i, line);
                    QuickUtils.debug(line);
                }

                lobby.show(player);
                QuickUtils.debug("Scoreboard updated for player " + player.getName());
                break;
            }
            case IN_QUEUE -> {
                lobby = new Board(scoreboardsConfig.getString("scoreboards.in-queue.title"));
                List<String> lines = scoreboardsConfig.getStringList("scoreboards.in-queue.lines");

                for (int i = 0; i < lines.size(); i++) {
                    Match playerMatch = playerManager.getPlayerMatch(gamePlayer);
                    String line = StringUtils.handleString(lines.get(i),
                            "{kills}", Integer.toString(persistencePlayerData.getKills()),
                            "{wins}", Integer.toString(persistencePlayerData.getWins()),
                            "{coins}", Integer.toString(persistencePlayerData.getCoins()),
                            "{level}", Integer.toString(persistencePlayerData.getLevel()),
                            "{xp}", Integer.toString(persistencePlayerData.getXp()),
                            "{mode}", playerMatch.getType().name(),
                            "{players}", Integer.toString(playerMatch.getPlayers().size()),
                            "{maxplayers}", Integer.toString(atlantis.getMainConfig().getInt("match.max-players")));

                    lobby.setLine(i, line);
                    QuickUtils.debug(line);
                }

                lobby.show(player);
                QuickUtils.debug("Scoreboard updated for player " + player.getName());
                break;
            }
            case PLAYING -> {
                Match playerMatch = playerManager.getPlayerMatch(gamePlayer);

                game = new Board(scoreboardsConfig.getString("scoreboards.game.title"));
                List<String> lines = scoreboardsConfig.getStringList("scoreboards.game.lines");

                for (int i = 0; i < lines.size(); i++) {
                    game.setLine(i, StringUtils.handleString(lines.get(i),
                            "{stage}", playerMatch.getStage().name(),
                            "{time}", Integer.toString(playerMatch.getNextStageTime()),
                            "{players}", Integer.toString(playerMatch.getPlayers().size()),
                            "{kills}", Integer.toString(gamePlayer.getMatchPlayerData().getKills()),
                            "{damage}", Double.toString(gamePlayer.getMatchPlayerData().getDamageDealt())));
                }

                game.show(player);
                break;
            }
        }
    }

    public void updateAll() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> update(PlayerManager.getInstance().getGamePlayer(player)));
    }
}
