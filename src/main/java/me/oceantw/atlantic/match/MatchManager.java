package me.oceantw.atlantic.match;


import com.google.common.collect.Sets;
import lombok.Getter;
import me.oceantw.atlantic.Atlantic;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class MatchManager {

    @Getter
    private final Set<Match> matches = Sets.newConcurrentHashSet();

    @Getter
    private final static MatchManager instance = new MatchManager();

    public void create(Match match) {
        matches.add(match);
    }

    public void countdown(Match match) {
        match.setStatus(MatchStatus.STARTING);
//        int timeleft;
//        BukkitTask task;
//        timeleft = 0;
//        task = new BukkitRunnable() {
//            @Override
//            public void run() {
//                // If count is less than or equal to ten, increment count, otherwise set count to -1.
//                timeleft = timeleft <= 10 ? timeleft + 1 : -1;
//                if (timeleft >= 10 || timeleft == -1) {
//                    task.cancel();
//                    return;
//                }
//                Bukkit.getLogger().log(Level.INFO, "Seconds passed: " + timeleft + "!";
//            }
//        }.runTaskTimer(Atlantic.getPlugin(Atlantic.class), 0, 20);

    }

    public void start(Match match) {
        match.setStatus(MatchStatus.INGAME);
    }

    public void deathmatch(Match match) {
        match.setStatus(MatchStatus.DEATHMATCH);
    }

    public void end(Match match) {
        match.setStatus(MatchStatus.ENDING);
    }

    public Optional<Match> findMatch(UUID uuid) {
        return matches.parallelStream().filter(match -> match.getUuid().equals(uuid)).findFirst();
    }
}
