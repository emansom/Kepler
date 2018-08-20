package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.moderation.Fuseright;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.user.ALERT;
import org.alexdev.kepler.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UsersOnlineCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add(Fuseright.DEFAULT);
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        int pageNumber = 1;
        int maxPlayersPerPage = 10;

        if (args.length > 0 && StringUtils.isNumeric(args[0])) {
            pageNumber = Integer.parseInt(args[0]);
        }

        List<Player> players = PlayerManager.getInstance().getPlayers();
        Map<Integer, List<Player>> paginatedPlayers = StringUtil.paginate(players, maxPlayersPerPage);

        if (!paginatedPlayers.containsKey(pageNumber - 1)) {
            pageNumber = 1;
        }

        Player session = (Player) entity;

        StringBuilder sb = new StringBuilder()
                .append("Users online: ").append(players.size()).append("<br>")
                .append("Daily player peak count: ").append(PlayerManager.getInstance().getDailyPlayerPeak()).append("<br>");

        if (paginatedPlayers.containsKey(pageNumber - 1)) {
            List<Player> playerList = paginatedPlayers.get(pageNumber - 1);

            for (Player player : playerList) {
                sb.append("\n - ");
                sb.append(player.getDetails().getName());
            }
        }

            sb.append("<br>").append("<br>Page ").append(pageNumber).append(" out of ").append(paginatedPlayers.size());
        session.send(new ALERT(sb.toString()));
    }

    @Override
    public String getDescription() {
        return "<page> - Get the list of players currently online";
    }
}
