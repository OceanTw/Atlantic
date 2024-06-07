package lol.oce.atlantis.match.types;

public enum MatchStage {
    PLAYING(1),
    DEATHMATCH(2),
    ENDING(3);

    public final int value;

    MatchStage(int value) {
        this.value = value;
    }

    public MatchStage next() {
        int nextIndex = ordinal() + 1;
        if (nextIndex >= values().length) {
            return null; // Return null if this is the last stage
        }
        return values()[nextIndex];
    }

}
