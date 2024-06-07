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

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Builder(setterPrefix = "set")
public class Match {
    private MatchStatus status;
    @Builder.Default
    private MatchStage stage = MatchStage.PLAYING;
    private boolean pvp;
    private MatchType type;
    private GamePlayer winner;
    private int time;

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
        Match match = matchManager.getMatches().stream()
                .filter(match1 -> match1.getUuid() == uuid)
                .findFirst()
                .orElse(null);
        PlayerManager.getInstance().setPlayerMatch(player, match);
        if (playersSize >= mainConfig.getInt("match.min-players-to-start")) {
            matchManager.countdown(this);
        }
        QuickUtils.debug("Player " + player.getPlayer().getName() + " joined the match");
    }

    public void end() {
        Atlantis atlantis = Atlantis.getInstance();
        Config messagesConfig = atlantis.getMessagesConfig();

        // send the stringlist
        for (GamePlayer gamePlayer : players) {
            PlayerManager.getInstance().setPlayerMatch(gamePlayer, null);
            List<String> lines = messagesConfig.getStringList("messages.match.end");
            for (String line : lines) {
                gamePlayer.getPlayer().sendMessage(line);
            }
        }
    }

    public void updateStage() {
        this.stage = this.stage.next();
    }
}
