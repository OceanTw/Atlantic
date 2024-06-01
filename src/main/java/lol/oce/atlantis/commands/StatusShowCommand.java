package lol.oce.atlantis.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import lol.oce.atlantis.player.PlayerManager;
import lol.oce.atlantis.types.PlayerStatus;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

@Command(name = "statusshow", aliases = {"ss"})
public class StatusShowCommand {
    @Execute
    @SuppressWarnings("unused")
    public void execute(@Context Player player) {
        player.sendMessage("Status: " + PlayerManager.getInstance().getPlayerStatus(PlayerManager.getInstance().getGamePlayer(player)));
        if (PlayerManager.getInstance().getPlayerStatus(PlayerManager.getInstance().getGamePlayer(player)) == PlayerStatus.PLAYING) {
            player.sendMessage("Match ID: " + PlayerManager.getInstance().getPlayerMatch(PlayerManager.getInstance().getGamePlayer(player)).getUuid());
        }
    }
}
