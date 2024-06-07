package lol.oce.atlantis.match;

import com.google.common.collect.Sets;
import de.leonhard.storage.Config;
import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.match.types.MatchStage;
import lol.oce.atlantis.match.types.MatchStatus;
import lol.oce.atlantis.match.types.MatchType;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.team.Team;
import lol.oce.atlantis.utils.QuickUtils;
import lol.oce.atlantis.utils.StringUtils;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Builder(setterPrefix = "set")
public class Match {
    private MatchStatus status;
    private boolean pvp;
    private MatchType type;
    private GamePlayer winner;
    @Builder.Default
    private MatchStage stage = MatchStage.PEACE;

    private final UUID uuid = UUID.randomUUID();
    private final Set<GamePlayer> players = Sets.newConcurrentHashSet();
    private final Set<Team> teams = Sets.newConcurrentHashSet();

    public void setup() {

    }

    public void join(GamePlayer player) {
        players.add(player);
        int playersSize = players.size();

        Atlantis atlantis = Atlantis.getInstance();
        MatchManager matchManager = MatchManager.getInstance();

        Config mainConfig = atlantis.getMainConfig();
        Config messagesConfig = atlantis.getMessagesConfig();

        player.getPlayer().sendMessage(StringUtils.handleString(
                messagesConfig.getString("messages.match.join"),

                "{0}",
                player.getPlayer().getName(),

                "{1}",
                Integer.toString(playersSize),

                "{2}",
                Integer.toString(mainConfig.getInt("match.min-players-to-start"))
        ));

        if (playersSize >= mainConfig.getInt("match.min-players-to-start")) {
            matchManager.countdown(this);
        }
        QuickUtils.debug("Player " + player.getPlayer().getName() + " joined the match");
    }

    public void end() {
        Atlantis atlantis = Atlantis.getInstance();
        Config messagesConfig = atlantis.getMessagesConfig();

        players.forEach(player -> player.getPlayer().sendMessage(StringUtils.handleString(
                messagesConfig.getString("messages.match.end"),

                "{0}",
                winner == null ? "DRAW" : winner.getPlayer().getName()
        )));
    }

    public int getNextStageTime() {
        Atlantis atlantis = Atlantis.getInstance();
        Config mainConfig = atlantis.getMainConfig();

        return switch (stage) {
            case PVP -> mainConfig.getInt("match.peace-duration");
            case DEATHMATCH -> mainConfig.getInt("match.duration");
            default -> 0;
        };
    }
}
