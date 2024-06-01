package lol.oce.atlantis.team;

import lol.oce.atlantis.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Team {
    List<GamePlayer> players;
    TeamColor color;
    int alivePlayers;
}
