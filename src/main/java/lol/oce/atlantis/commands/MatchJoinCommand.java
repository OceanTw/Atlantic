package lol.oce.atlantis.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lol.oce.atlantis.match.MatchManager;
import lol.oce.atlantis.match.types.MatchStatus;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.types.PlayerStatus;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Command(name = "matchjoin", aliases = {"j"})
public class MatchJoinCommand {
    @Execute
    @SuppressWarnings("unused")
    @Permission("atlantic.admin")
    public void execute(@Context Player player, @Nullable @OptionalArg String matchId) {
        Optional.ofNullable(matchId).map(matchId1 -> PlayerManager.getInstance()).ifPresent(playerManager -> {

            // TODO: filter: match id equals, maybe?
            MatchManager.getInstance().getMatches().stream()
                    .filter(match -> match.getStatus().equals(MatchStatus.WAITING))
                    .findFirst()
                    .ifPresent(match -> match.join(playerManager.getGamePlayer(player)));
            playerManager.setPlayerStatus(playerManager.getGamePlayer(player), PlayerStatus.IN_QUEUE);
        });
    }
}
