package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class FULLGAMESTATUS extends MessageComposer {
    private final Game game;

    public FULLGAMESTATUS(Game game) {
        this.game = game;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(1);
        response.writeInt(this.game.getPreparingGameSecondsLeft().get());
        response.writeInt(Game.PREPARING_GAME_SECONDS_LEFT);
        response.writeInt(0);
        response.writeInt(this.game.getRoomModel().getMapSizeX());
        response.writeInt(this.game.getRoomModel().getMapSizeY());

        for (int y = 0; y < this.game.getRoomModel().getMapSizeY(); y++) {
            for (int x = 0; x < this.game.getRoomModel().getMapSizeX(); x++) {
                response.writeInt(this.game.getTileColours()[x][y].getTileColourId());
                response.writeInt(this.game.getTileStates()[x][y].getTileStateId());
            }
        }

        response.writeInt(1);
        response.writeInt(0);
    }

    @Override
    public short getHeader() {
        return 243; // "Cs"
    }
}
