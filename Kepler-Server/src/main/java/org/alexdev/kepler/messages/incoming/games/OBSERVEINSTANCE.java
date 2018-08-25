package org.alexdev.kepler.messages.incoming.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.games.GameState;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.triggers.GameLobbyTrigger;
import org.alexdev.kepler.messages.outgoing.games.GAMEINSTANCE;
import org.alexdev.kepler.messages.outgoing.games.JOINFAILED;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.util.*;

public class OBSERVEINSTANCE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        if (!(room.getModel().getModelTrigger() instanceof GameLobbyTrigger)) {
            return;
        }

        int gameId = reader.readInt();

        Game game = GameManager.getInstance().getGameById(gameId);

        if (game == null) {
            return;
        }

        GamePlayer gamePlayer = player.getRoomUser().getGamePlayer();

        if (gamePlayer != null) {
            return;
        }

        if (game.getGameState() == GameState.WAITING) {
            if (player.getDetails().getTickets() <= 1) {
                player.send(new JOINFAILED(JOINFAILED.FailedReason.TICKETS_NEEDED));
                return;
            }

            // Find team with lowest team members to add to
            List<GameTeam> sortedTeamList = new ArrayList<>(game.getTeamPlayers().values());
            sortedTeamList.sort(Comparator.comparingInt(team -> team.getPlayerList().size()));

            // Select game team
            GameTeam gameTeam = sortedTeamList.get(0);

            if (gameTeam == null) {
                return;
            }

            if (!game.canSwitchTeam(gameTeam.getId())) {
                return;
            }

            player.getRoomUser().setGamePlayer(new GamePlayer(player));
            player.getRoomUser().getGamePlayer().setGameId(game.getId());
            player.getRoomUser().getGamePlayer().setTeamId(gameTeam.getId());

            game.movePlayer(player, -1, gameTeam.getId());
            game.send(new GAMEINSTANCE(game));
        }
    }
}
