package lol.oce.atlantis.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lol.oce.atlantis.match.MatchManager;
import lol.oce.atlantis.match.MatchStatus;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.types.PlayerStatus;
import org.bukkit.entity.Player;

@Command(name = "matchjoin", aliases = {"j"})
public class MatchJoinCommand {
    @Execute
    @Permission("atlantic.admin")
    public void execute(@Context Player player, @OptionalArg String matchId) {
        if (matchId == null) {
            // Join the first available match
            MatchManager.getInstance().getMatches().stream()
                    .filter(match -> match.getStatus().equals(MatchStatus.WAITING))
                    .findFirst()
                    .ifPresent(match -> match.join(PlayerManager.getInstance().getGamePlayer(player)));
            PlayerManager.getInstance().setPlayerStatus(PlayerManager.getInstance().getGamePlayer(player),
                    PlayerStatus.WAITING);
        } else {
            // Join the match with the given ID
        }
    }
}
