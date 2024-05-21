package me.oceantw.atlantic.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.oceantw.atlantic.match.MatchManager;
import me.oceantw.atlantic.match.MatchStatus;
import me.oceantw.atlantic.player.GamePlayer;
import me.oceantw.atlantic.player.PlayerManager;
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
        } else {
            // Join the match with the given ID
        }
    }
}
