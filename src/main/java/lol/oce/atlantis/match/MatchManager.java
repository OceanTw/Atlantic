package lol.oce.atlantis.match;


import com.google.common.collect.Sets;
import lol.oce.atlantis.Atlantis;
import lol.oce.atlantis.match.types.MatchStatus;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MatchManager {

    private BukkitTask task;

    @Getter
    private final Set<Match> matches = Sets.newConcurrentHashSet();

    @Getter
    private final static MatchManager instance = new MatchManager();

    public void create(Match match) {
        matches.add(match);
    }

    public void countdown(Match match) {
        match.setStatus(MatchStatus.STARTING);

        task = new BukkitRunnable() {
            int seconds = Atlantis.getInstance().getMainConfig().getInt("match.countdown");

            @Override
            public void run() {
                if (seconds == 0) {
                    start(match);
                    cancel();
                    return;
                }
                seconds--;
            }
        }.runTaskTimerAsynchronously(Atlantis.getInstance(), 0, 20L);

    }

    public void start(Match match) {
        match.setStatus(MatchStatus.INGAME);

        task = new BukkitRunnable() {
            int seconds = Atlantis.getInstance().getMainConfig().getInt("match.duration");

            @Override
            public void run() {
                if (seconds == 0) {
                    deathmatch(match);
                    cancel();
                    return;
                }
                seconds--;
            }
        }.runTaskTimerAsynchronously(Atlantis.getInstance(), 0, 20L);
    }

    public void deathmatch(Match match) {
        match.setStatus(MatchStatus.DEATHMATCH);
        task = new BukkitRunnable() {
            int seconds = Atlantis.getInstance().getMainConfig().getInt("match.deathmatch-duration");

            @Override
            public void run() {
                if (seconds == 0) {
                    end(match);
                    cancel();
                    return;
                }
                seconds--;
            }
        }.runTaskTimerAsynchronously(Atlantis.getInstance(), 0, 20L);
    }

    public void end(Match match) {
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
        }.runTaskTimerAsynchronously(Atlantis.getInstance(), 0, 20L);
    }

    public Optional<Match> findMatch(UUID uuid) {
        return matches.parallelStream().filter(match -> match.getUuid().equals(uuid)).findFirst();
    }
}
