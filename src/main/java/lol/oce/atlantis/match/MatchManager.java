package lol.oce.atlantis.match;

import com.google.common.collect.Sets;
import de.leonhard.storage.Config;
import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.match.types.MatchStage;
import lol.oce.atlantis.match.types.MatchStatus;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.types.PlayerStatus;
import lol.oce.atlantis.utils.QuickUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchManager {
    @Getter
    private static final MatchManager instance = new MatchManager();

    @Getter
    private final Set<Match> matches = Sets.newConcurrentHashSet();

    private BukkitTask task;

    public void create(Match match) {
        matches.add(match);
    }

    public void countdown(Match match) {
        executeTimedTask(match, MatchStatus.STARTING, "match.countdown", this::start);
        QuickUtils.debug("Match " + match.getUuid() + " is starting!");
    }

    public void start(Match match) {
        QuickUtils.debug("Match " + match.getUuid() + " has started!");
        executeTimedTask(match, MatchStatus.INGAME, "match.duration", m -> {
            deathMatch(m);
            m.setStage(MatchStage.DEATHMATCH);
        });
        match.setStatus(MatchStatus.INGAME);
        match.getPlayers().forEach(player -> PlayerManager.getInstance().setPlayerStatus(player, PlayerStatus.PLAYING));
    }

    public void deathMatch(Match match) {
        QuickUtils.debug("Match " + match.getUuid() + " is in deathmatch!");
        executeTimedTask(match, MatchStatus.DEATHMATCH, "match.deathmatch-duration", m -> {
            end(m);
            m.setStage(MatchStage);
        });
        match.setStatus(MatchStatus.DEATHMATCH);
    }

    public void end(Match match) {
        QuickUtils.debug("Match " + match.getUuid() + " is ending!");
        executeTimedTask(match, MatchStatus.ENDING, 15, m -> {
            matches.remove(m);
            m.end();
        });
        match.setStatus(MatchStatus.ENDING);
    }

    public Optional<Match> findMatch(UUID uuid) {
        return matches.stream().filter(match -> match.getUuid().equals(uuid)).findFirst();
    }

    private void executeTimedTask(Match match, MatchStatus status, String configPath, TaskCompletionHandler completionHandler) {
        Atlantis atlantis = Atlantis.getInstance();
        Config mainConfig = atlantis.getMainConfig();
        int duration = mainConfig.getInt(configPath);
        executeTimedTask(match, status, duration, completionHandler);
    }

    private void executeTimedTask(Match match, MatchStatus status, int duration, TaskCompletionHandler completionHandler) {
        Atlantis atlantis = Atlantis.getInstance();
        match.setStatus(status);

        task = new BukkitRunnable() {
            int seconds = duration;

            @Override
            public void run() {
                if (seconds == 0) {
                    completionHandler.complete(match);
                    cancel();
                }
                seconds--;
                match.setTime(seconds);
            }
        }.runTaskTimerAsynchronously(atlantis, 0, 20L);
    }

    @FunctionalInterface
    private interface TaskCompletionHandler {
        void complete(Match match);
    }
}
