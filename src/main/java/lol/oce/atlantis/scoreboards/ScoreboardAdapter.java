package lol.oce.atlantis.scoreboards;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.match.Match;
import lol.oce.atlantis.match.types.MatchStage;
import lol.oce.atlantis.match.types.MatchStatus;
import lol.oce.atlantis.match.types.MatchType;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PersistencePlayerData;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.types.PlayerStatus;
import lol.oce.atlantis.utils.QuickUtils;
import lol.oce.atlantis.utils.StringUtils;
import lol.oce.atlantis.scoreboards.scoreboards.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(player);
        if (PlayerManager.getInstance().getPlayerStatus(gamePlayer) == null) {
            PlayerManager.getInstance().setPlayerStatus(gamePlayer, PlayerStatus.LOBBY);
        }
        switch (PlayerManager.getInstance().getPlayerStatus(gamePlayer)) {
            case LOBBY:
                return Atlantis.getInstance().getScoreboardsConfig().getString("scoreboards.lobby.title");
            case IN_QUEUE:
                return Atlantis.getInstance().getScoreboardsConfig().getString("scoreboards.in-queue.title");
            case PLAYING:
                if (PlayerManager.getInstance().getPlayerMatch(gamePlayer).getType() == MatchType.TEAM) {
                    return Atlantis.getInstance().getScoreboardsConfig().getString("scoreboards.game-team.title");
                } else {
                    return Atlantis.getInstance().getScoreboardsConfig().getString("scoreboards.game-solo.title");
                }
            case SPECTATING:
                return Atlantis.getInstance().getScoreboardsConfig().getString("scoreboards.spectating.title");
            default:
                return null;
        }
    }

    @Override
    public List<String> getLines(Player player) {
        GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(player);
        PersistencePlayerData persistencePlayerData = gamePlayer.getPersistencePlayerData();
        List<String> lines;

        switch (PlayerManager.getInstance().getPlayerStatus(gamePlayer)) {
            case LOBBY:
                lines = Atlantis.getInstance().getScoreboardsConfig().getStringList("scoreboards.lobby.lines");
                break;
            case IN_QUEUE:
                lines = Atlantis.getInstance().getScoreboardsConfig().getStringList("scoreboards.in-queue.lines");
                break;
            case PLAYING:
                if (PlayerManager.getInstance().getPlayerMatch(gamePlayer).getType() == MatchType.TEAM) {
                    lines = Atlantis.getInstance().getScoreboardsConfig().getStringList("scoreboards.game-team.lines");
                } else {
                    lines = Atlantis.getInstance().getScoreboardsConfig().getStringList("scoreboards.game-solo.lines");
                }
                break;
            case SPECTATING:
                lines = Atlantis.getInstance().getScoreboardsConfig().getStringList("scoreboards.spectating.lines");
                break;
            default:
                return null;
        }

        List<String> processedLines = new ArrayList<>();
        Match playerMatch = PlayerManager.getInstance().getPlayerMatch(gamePlayer);
        String[] placeholders = getPlayerPlaceholders(persistencePlayerData, gamePlayer, playerMatch);
        for (String line : lines) {
            processedLines.add(StringUtils.handleString(line, placeholders));
        }

        return processedLines;
    }

    private String[] getPlayerPlaceholders(PersistencePlayerData data, GamePlayer gamePlayer, Match match) {
        if (data == null) {
            return new String[0];
        }

        int kills = data.getKills();
        int wins = data.getWins();
        int coins = data.getCoins();
        int level = data.getLevel();
        int xp = data.getXp();

        List<String> placeholders = new ArrayList<>();
        placeholders.add("{kills}");
        placeholders.add(Integer.toString(kills));
        placeholders.add("{wins}");
        placeholders.add(Integer.toString(wins));
        placeholders.add("{coins}");
        placeholders.add(Integer.toString(coins));
        placeholders.add("{level}");
        placeholders.add(Integer.toString(level));
        placeholders.add("{xp}");
        placeholders.add(Integer.toString(xp));
        placeholders.add("{maxplayers}");
        placeholders.add(Atlantis.getInstance().getMainConfig().getInt("match.max-players") + "");

        if (match != null) {
            placeholders.add("{stage}");
            // get the next stage
            MatchStage nextStage = match.getStage().next();
            // Add a logging statement to print the next stage
            QuickUtils.debug("Next stage: " + (nextStage == null ? "END" : nextStage.name()));
            placeholders.add(nextStage == null ? "END" : nextStage.name());
            placeholders.add("{players}");
            placeholders.add(Integer.toString(match.getPlayers().size()));
            placeholders.add("{teams}");
            placeholders.add(Integer.toString(match.getTeams().size()));
            placeholders.add("{damage}");
            placeholders.add(Double.toString(gamePlayer.getMatchPlayerData().getDamageDealt()));
            placeholders.add("{mode}");
            placeholders.add(match.getType().name());
            placeholders.add("{matchkills}");
            placeholders.add(Integer.toString(kills));
            placeholders.add("{countdown}");
            placeholders.add(Integer.toString(match.getTime()));
        }


        return placeholders.toArray(new String[0]);
    }
}
