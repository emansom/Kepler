package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.ArrayList;
import java.util.List;

public class FULLGAMESTATUS extends MessageComposer {
    private final Game game;
    private final List<GamePlayer> gamePlayerList;
    private final GamePlayer gamePlayer;

    public FULLGAMESTATUS(Game game, GamePlayer gamePlayer) {
        this.game = game;
        this.gamePlayerList = new ArrayList<>();
        this.gamePlayer = gamePlayer;

        for (GameTeam team : this.game.getTeams().values()) {
            this.gamePlayerList.addAll(team.getActivePlayers());
        }
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.game.getGameState().getStateId());
        response.writeInt(this.game.getPreparingGameSecondsLeft().get());
        response.writeInt(GameManager.getInstance().getPreparingSeconds(game.getGameType()));
        response.writeInt(this.game.getObjects().size()); // TODO: Objects here

        if (this.game.getGameType() == GameType.BATTLEBALL) {
            for (var gameObject : this.game.getObjects()) {
                response.writeInt(gameObject.getGameObjectType().getObjectId()); // type, 0 = player
                gameObject.serialiseObject(response);
            }

            response.writeInt(this.game.getRoomModel().getMapSizeY());
            response.writeInt(this.game.getRoomModel().getMapSizeX());

            for (int y = 0; y < this.game.getRoomModel().getMapSizeY(); y++) {
                for (int x = 0; x < this.game.getRoomModel().getMapSizeX(); x++) {
                    BattleballTile tile = (BattleballTile) this.game.getTile(x, y);

                    if (tile == null) {
                        response.writeInt(-1);
                        response.writeInt(0);
                    } else {
                        response.writeInt(tile.getColour().getColourId());
                        response.writeInt(tile.getState().getTileStateId());
                    }
                }
            }

            response.writeInt(1);
            response.writeInt(0); // TODO: Show events on game load
        }
    }

    @Override
    public short getHeader() {
        return 243; // "Cs"
    }
}
