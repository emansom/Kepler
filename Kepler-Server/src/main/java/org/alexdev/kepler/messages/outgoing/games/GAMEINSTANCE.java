package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class GAMEINSTANCE extends MessageComposer {
    private final Game game;

    public GAMEINSTANCE(Game game) {
        this.game = game;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.game.getGameState().getStateId());
        response.writeBool(this.game.canGameStart());
        response.writeString(this.game.getName());
        response.writeInt(this.game.getGameCreator().getRoomUser().getInstanceId());
        response.writeString(this.game.getGameCreator().getDetails().getName());

        // TODO: Special SnowStorm parameters

        response.writeInt(this.game.getMapId());
        response.writeInt(this.game.getSpectators().size());
        response.writeInt(this.game.getTeamAmount());

        for (int i = 0; i < this.game.getTeamAmount(); i++) {
            List<GamePlayer> playerList = this.game.getTeams().get(i).getPlayers();

            response.writeInt(playerList.size());

            for (GamePlayer player : playerList) {
                response.writeInt(player.getPlayer().getRoomUser().getInstanceId());
                response.writeString(player.getPlayer().getDetails().getName());
            }
        }

        // TODO: Special SnowStorm parameters

        String[] powerUps = new String[this.game.getPowerUps().size()];

        for (int i = 0; i < this.game.getPowerUps().size(); i++) {
            powerUps[i] = String.valueOf(this.game.getPowerUps().get(i));
        }

        response.writeString(String.join(",", powerUps));
    }

    @Override
    public short getHeader() {
        return 233; // "Ci"
    }
}
