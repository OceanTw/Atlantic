package lol.oce.atlantis.match;

import com.google.common.collect.Sets;
import de.leonhard.storage.Config;
import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.match.types.MatchStatus;
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
    }

    public void start(Match match) {
        executeTimedTask(match, MatchStatus.INGAME, "match.duration", this::deathMatch);
    }

    public void deathMatch(Match match) {
        executeTimedTask(match, MatchStatus.DEATHMATCH, "match.deathmatch-duration", this::end);
    }

    public void end(Match match) {
        executeTimedTask(match, MatchStatus.ENDING, 15, m -> {
            matches.remove(m);
            m.end();
        });
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
            }
        }.runTaskTimerAsynchronously(atlantis, 0, 20L);
    }

    @FunctionalInterface
    private interface TaskCompletionHandler {
        void complete(Match match);
    }
}
