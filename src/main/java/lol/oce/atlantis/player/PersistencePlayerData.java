package lol.oce.atlantis.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain=true)
public class PersistencePlayerData {
    private int kills;
    private int deaths;
    private int wins;
    private int games;
    private int level;
    private int xp;
    private int coins;

    public static PersistencePlayerData createDefault() {
        return new PersistencePlayerData(0, 0, 0, 0, 1, 0, 0);
    }
}

