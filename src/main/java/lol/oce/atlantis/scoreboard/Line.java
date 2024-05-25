package lol.oce.atlantis.scoreboard;

import org.bukkit.scoreboard.Team;

class Line {
    private final String defaultEntry;
    private String fixColor = "";

    private Team team;
    private final int line;

    private boolean isSet = false;

    Line(String entry, Team team, int line) {
        this.team = team;
        this.line = line;
        this.defaultEntry = entry;

        updateEntry();
    }

    private void updateEntry() {
        if (defaultEntry != null) {
            team.removeEntry(getEntry());
        }
        team.addEntry(getEntry());
    }

    void fixColor(String color) {
        fixColor = color;
        updateEntry();
    }

    String getFixColor() {
        return fixColor;
    }

    String getEntry() {
        return defaultEntry + fixColor;
    }

    Team getTeam() {
        return team;
    }

    int getLine() {
        return line;
    }

    boolean isSet() {
        return isSet;
    }

    void setSet(boolean set) {
        isSet = set;
    }
}