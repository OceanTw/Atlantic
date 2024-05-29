package lol.oce.atlantis.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO: plan 4 code refactoring
public class Board {
    private static final List<ChatColor> COLORS = Arrays.asList(ChatColor.values());
    private static int count = 0;
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private final Objective objective;
    private final List<Line> lines = new ArrayList<>();
    private String title;

    public Board(String title) {
        objective = scoreboard.registerNewObjective("scorelib" + count++, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setTitle(title);

        for (int i = 0; i < COLORS.size(); ++i) {
            final ChatColor color = COLORS.get(i);

            final Team team = scoreboard.registerNewTeam("line" + i);

            lines.add(i, new Line(color.toString(), team, i));
        }
    }

    public void setLines(String... lines) {
        int start = lines.length;
        for (String line : lines) {
            setLine(start--, line);
        }
    }

    public void setLine(final int line, String value) {
        final Line boardLine = getLine(line);
        if (boardLine == null) {
            return;
        }

        updateLine(boardLine, value);

        if (!boardLine.isSet()) {
            objective.getScore(boardLine.getEntry()).setScore(line);
            boardLine.setSet(true);
        }
    }

    private void updateLine(final Line boardLine, String value) {
        value = ChatColor.translateAlternateColorCodes('&', value);

        if (value.length() <= 16) {
            boardLine.getTeam().setPrefix(value);
            return;
        }

        final String prefix = value.substring(0, 16);
        String suffix = value.substring(16);

        final String lastColor = ChatColor.getLastColors(prefix);
        if (!boardLine.getFixColor().equals(lastColor)) {
            removeLine(boardLine);
            boardLine.fixColor(lastColor);
        }

        if (suffix.length() > 16) {
            suffix = suffix.substring(0, 16);
        }

        boardLine.getTeam().setPrefix(prefix);
        boardLine.getTeam().setSuffix(suffix);
    }

    public void removeLine(int line) {
        final Line boardLine = getLine(line);
        if (boardLine == null) {
            return;
        }
        removeLine(boardLine);
    }

    private void removeLine(final Line line) {
        scoreboard.resetScores(line.getEntry());
        line.setSet(false);
    }

    private Line getLine(int index) {
        return lines.stream().filter(line -> line.getLine() == index).findFirst().orElse(null);
    }

    public void show(Player player) {
        player.setScoreboard(scoreboard);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
    }
}