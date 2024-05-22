package me.oceantw.atlantic.match;

import lombok.Builder;
import lombok.Data;
import me.oceantw.atlantic.Atlantic;
import me.oceantw.atlantic.player.GamePlayer;
import me.oceantw.atlantic.utils.StringUtils;

import java.util.List;
import java.util.UUID;

@Data
@Builder(setterPrefix = "set")
public class Match {
    private UUID uuid;
    private MatchStatus status;
    private MatchType type;
    private List<GamePlayer> players;

    public void join(GamePlayer player) {
        // Join the match
        players.add(player);
        player.getPlayer().sendMessage(StringUtils.handleString(
                Atlantic.getInstance().getMessagesConfig().getString("messages.match.join")));
        if (players.size() >= Atlantic.getInstance().getMainConfig().getInt("match.min-players-to-start")) {
            MatchManager.getInstance().countdown(this);
        }
    }
}
