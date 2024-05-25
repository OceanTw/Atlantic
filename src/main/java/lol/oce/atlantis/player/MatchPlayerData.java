package lol.oce.atlantis.player;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MatchPlayerData {
    private int kills;
    private int damageDealt;

    public static MatchPlayerData createDefault() {
        return new MatchPlayerData(0, 0);
    }
}

