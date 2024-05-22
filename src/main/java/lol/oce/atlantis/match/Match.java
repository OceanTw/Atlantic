package lol.oce.atlantis.match;

import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.utils.StringUtils;
import lombok.Builder;
import lombok.Data;

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
        // Join the match
        players.add(player);
        player.getPlayer().sendMessage(StringUtils.handleString(
                Atlantis.getInstance().getMessagesConfig().getString("messages.match.join")));
        if (players.size() >= Atlantis.getInstance().getMainConfig().getInt("match.min-players-to-start")) {
            MatchManager.getInstance().countdown(this);
        }
    }

    public void end() {
        // End the match
        players.forEach(player -> player.getPlayer().sendMessage(StringUtils.handleString(
                Atlantis.getInstance().getMessagesConfig().getString("messages.match.end"))));
    }

}
