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
        Atlantis atlantis = Atlantis.getInstance();
        Config mainConfig = atlantis.getMainConfig();

        match.setStatus(MatchStatus.STARTING);

        task = new BukkitRunnable() {
            int seconds = mainConfig.getInt("match.countdown");

            @Override
            public void run() {
                if (seconds == 0) {
                    start(match);
                    cancel();
                    return;
                }
                seconds--;
            }
        }.runTaskTimerAsynchronously(atlantis, 0, 20L);

    }

    public void start(Match match) {
        Atlantis atlantis = Atlantis.getInstance();
        Config mainConfig = atlantis.getMainConfig();

        match.setStatus(MatchStatus.INGAME);

        task = new BukkitRunnable() {
            int seconds = mainConfig.getInt("match.duration");

            @Override
            public void run() {
                if (seconds == 0) {
                    deathMatch(match);
                    cancel();
                    return;
                }
                seconds--;
            }
        }.runTaskTimerAsynchronously(atlantis, 0, 20L);
    }

    public void deathMatch(Match match) {
        Atlantis atlantis = Atlantis.getInstance();
        Config mainConfig = atlantis.getMainConfig();

        match.setStatus(MatchStatus.DEATHMATCH);

        task = new BukkitRunnable() {
            int seconds = mainConfig.getInt("match.deathmatch-duration");

            @Override
            public void run() {
                if (seconds == 0) {
                    end(match);
                    cancel();
                    return;
                }
                seconds--;
            }
        }.runTaskTimerAsynchronously(atlantis, 0, 20L);
    }

    public void end(Match match) {
        Atlantis atlantis = Atlantis.getInstance();

        match.setStatus(MatchStatus.ENDING);

        task = new BukkitRunnable() {
            int seconds = 15;

            @Override
            public void run() {
                if (seconds == 0) {
                    matches.remove(match);
                    match.end();
                    cancel();
                    return;
                }
                seconds--;
            }
        }.runTaskTimerAsynchronously(atlantis, 0, 20L);
    }

    public Optional<Match> findMatch(UUID uuid) {
        return matches.stream()
                .filter(match -> match.getUuid().equals(uuid)).findFirst();
    }
}
