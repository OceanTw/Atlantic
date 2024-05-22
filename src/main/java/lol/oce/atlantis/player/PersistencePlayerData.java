package lol.oce.atlantis.player;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PersistencePlayerData {
    private int kills;
    private int deaths;
    private int wins;
    private int games;

    public static PersistencePlayerData createDefault() {
        return new PersistencePlayerData(0, 0, 0, 0);
    }

//    public static PersistencePlayerData create(int kills, int deaths, int wins, int games) {
//        return new PersistencePlayerData(kills, deaths, wins, games);
//    }
}

