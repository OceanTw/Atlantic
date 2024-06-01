package lol.oce.atlantis.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lol.oce.atlantis.player.GamePlayer;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.utils.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Command(name = "statsupdate", aliases = {"su"})
public class StatsUpdateCommand {
    @Execute
    @SuppressWarnings("unused")
    @Permission("atlantic.admin")
    public void execute(@Context Player player, @Nullable @OptionalArg String matchId) {
        PlayerManager.getInstance().getPlayers().forEach(GamePlayer::update);
        player.sendMessage(StringUtils.handleString("&aStats updated for all players."));
    }
}
