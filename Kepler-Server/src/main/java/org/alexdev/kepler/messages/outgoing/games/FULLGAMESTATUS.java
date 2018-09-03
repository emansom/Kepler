package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.games.snowstorm.SnowStormGame;
import org.alexdev.kepler.game.games.snowstorm.object.SnowStormPlayerObject;
import org.alexdev.kepler.game.games.snowstorm.object.SnowStormSpawnPlayerEvent;
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
        response.writeInt(this.gamePlayerList.size()); // TODO: Objects here

        if (this.game.getGameType() == GameType.SNOWSTORM) {
            List<GameObject> objects = new ArrayList<>();

            for (var team : this.game.getTeams().values()) {
                for (var gamePlayer : team.getActivePlayers()) {
                    response.writeInt(GameObjectType.SNOWSTORM_PLAYER_OBJECT.getObjectId());

                    GameObject obj = new SnowStormPlayerObject(gamePlayer);
                    obj.serialiseObject(response);

                    objects.add(new SnowStormSpawnPlayerEvent(gamePlayer));
                }
            }

            response.writeBool(false);
            response.writeInt(this.game.getTeamAmount());

            new SNOWSTORM_GAMESTATUS((SnowStormGame) this.game, objects, this.gamePlayer).compose(response);
        }

        if (this.game.getGameType() == GameType.BATTLEBALL) {
            for (var team : this.game.getTeams().values()) {
                for (var gamePlayer : team.getActivePlayers()) {
                    //if (!this.startedGame) {
                    response.writeInt(0); // type, 0 = player
                    response.writeInt(gamePlayer.getPlayer().getDetails().getId());
                    response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getX());
                    response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getY());
                    response.writeInt((int) gamePlayer.getPlayer().getRoomUser().getPosition().getZ());
                    response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getRotation());
                    response.writeInt(0);
                    response.writeInt(-1);
                    response.writeString(gamePlayer.getPlayer().getDetails().getName());
                    response.writeString(gamePlayer.getPlayer().getDetails().getMotto());
                    response.writeString(gamePlayer.getPlayer().getDetails().getFigure());
                    response.writeString(gamePlayer.getPlayer().getDetails().getSex());
                    response.writeInt(gamePlayer.getTeamId());
                    response.writeInt(gamePlayer.getPlayer().getDetails().getId()); // Actually room user id/instance id
                }
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
            response.writeInt(this.game.getPersistentEvents().size());

            for (GameEvent event : this.game.getPersistentEvents()) {
                response.writeInt(event.getGameEventType().getEventId());
                event.serialiseEvent(response);
            }
        }
    }

    @Override
    public short getHeader() {
        return 243; // "Cs"
    }
}
