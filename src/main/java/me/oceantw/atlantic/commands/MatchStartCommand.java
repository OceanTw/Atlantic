package me.oceantw.atlantic.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.oceantw.atlantic.match.Match;
import me.oceantw.atlantic.match.MatchManager;
import me.oceantw.atlantic.match.MatchStatus;
import me.oceantw.atlantic.match.MatchType;
import me.oceantw.atlantic.utils.QuickUtils;
import org.bukkit.entity.Player;

@Command(name = "matchstart", aliases = {"s"})
public class MatchStartCommand {
    @Execute
    @Permission("atlantic.admin")
    public void execute(@Context Player player, @Arg MatchType type){
        QuickUtils.debug("Player " + player.getName() + " started a match of type " + type.name());
        Match match = Match.builder().setType(type).setStatus(MatchStatus.WAITING).build();
        MatchManager.getInstance().create(match);
    }
}
