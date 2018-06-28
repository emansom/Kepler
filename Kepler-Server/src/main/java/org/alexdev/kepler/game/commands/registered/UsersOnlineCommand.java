package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.Kepler;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.user.ALERT;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.StringUtil;

import java.util.Collection;
import java.util.List;

public class UsersOnlineCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add("default");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        int pageNumber = 0;
        int maxPlayersPerPage = 8;

        if (args.length > 0 && StringUtil.isNumber(args[0])) {
            pageNumber = Integer.parseInt(args[0]);
        }

        Player session = (Player) entity;

        List<Player> players = PlayerManager.getInstance().getPlayers();
        List<List<Player>> paginatedPlayers = StringUtil.paginate(players, maxPlayersPerPage);

        StringBuilder sb = new StringBuilder()
                .append("Users online: ").append(players.size()).append("\n");

        if (paginatedPlayers.size() >= pageNumber) {
            List<Player> playerList = paginatedPlayers.get(pageNumber);

            for (Player player : playerList) {
                sb.append("\n - ");
                sb.append(player.getDetails().getName());
            }
        }

        sb.append("\n").append("\nPage numbers: 0 - ").append(pageNumber);

        session.send(new ALERT(sb.toString()));
    }

    @Override
    public String getDescription() {
        return "Get the uptime and status of the server";
    }
}
