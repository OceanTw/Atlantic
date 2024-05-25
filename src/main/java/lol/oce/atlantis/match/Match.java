package lol.oce.atlantis.match;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.utils.QuickUtils;
import lol.oce.atlantis.utils.StringUtils;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
@Builder(setterPrefix = "set")
public class Match {
    private UUID uuid;
    private MatchStatus status;
    private boolean pvp;
    private MatchType type;
    private GamePlayer winner;
    private List<GamePlayer> players;

    public void join(GamePlayer player) {

        players = players == null ? new ArrayList<>() : players;
        players.add(player);
        player.getPlayer().sendMessage(StringUtils.handleString(
                Atlantis.getInstance().getMessagesConfig().getString("messages.match.join"),
                "{0}", player.getPlayer().getName(),
                "{1}", Integer.toString(players.size()),
                "{2}", Integer.toString(Atlantis.getInstance().getMainConfig().getInt(
                        "match.min-players-to-start"))));
        if (players.size() >= Atlantis.getInstance().getMainConfig().getInt("match.min-players-to-start")) {
            MatchManager.getInstance().countdown(this);
        }
        QuickUtils.debug("Player " + player.getPlayer().getName() + " joined the match");
    }

    public void end() {

        players.forEach(player -> player.getPlayer().sendMessage(StringUtils.handleString(
                Atlantis.getInstance().getMessagesConfig().getString("messages.match.end"),
                "{0}", winner == null ? "DRAW" : winner.getPlayer().getName())));
    }

}
