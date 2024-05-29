package lol.oce.atlantis.scoreboard;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scoreboard.Team;

import java.util.Optional;

@Getter
@Setter
public class Line {
    private final String defaultEntry;
    private final int line;
    private String fixColor = "";
    private final Team team;
    private boolean set = false;

    public Line(String entry, Team team, int line) {
        this.team = team;
        this.line = line;
        this.defaultEntry = entry;

        updateEntry();
    }

    private void updateEntry() {
        Optional.ofNullable(defaultEntry).ifPresentOrElse(
                entry -> team.removeEntry(getEntry()), () -> team.addEntry(getEntry())
        );
    }

    public void fixColor(String color) {
        fixColor = color;
        updateEntry();
    }

    public String getEntry() {
        return defaultEntry + fixColor;
    }
}