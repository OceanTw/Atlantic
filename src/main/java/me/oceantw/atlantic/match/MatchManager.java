package me.oceantw.atlantic.match;


import com.google.common.collect.Sets;
import lombok.Getter;
import me.oceantw.atlantic.Atlantic;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
