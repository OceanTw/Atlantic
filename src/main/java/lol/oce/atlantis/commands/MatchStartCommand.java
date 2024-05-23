package lol.oce.atlantis.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lol.oce.atlantis.match.Match;
import lol.oce.atlantis.match.MatchManager;
import lol.oce.atlantis.match.MatchStatus;
import lol.oce.atlantis.match.MatchType;
import lol.oce.atlantis.utils.QuickUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Command(name = "matchstart", aliases = {"s"})
public class MatchStartCommand {
    @Execute
    @Permission("atlantic.admin")
    public void execute(@Context Player player, @Arg MatchType type) {
        QuickUtils.debug("Player " + player.getName() + " started a match of type " + type.name());
        Match match = Match.builder().setType(type).setStatus(MatchStatus.WAITING).setPvp(false).build();
        MatchManager.getInstance().create(match);
    }
}
