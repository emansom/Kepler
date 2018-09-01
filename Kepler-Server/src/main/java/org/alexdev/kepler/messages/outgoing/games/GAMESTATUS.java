package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.Collection;
import java.util.List;

public class GAMESTATUS extends MessageComposer {
    private final Game game;

    private final Collection<GameTeam> gameTeams;

    private final List<GameObject> objects;
    private final List<GameEvent> events;

    private final List<BattleballTile> updateTiles;
    private final List<BattleballTile> fillTiles;

    public GAMESTATUS(Game game, Collection<GameTeam> gameTeams, List<GameObject> objects, List<GameEvent> events, List<BattleballTile> updateTiles, List<BattleballTile> fillTiles) {
        this.game = game;
        this.gameTeams = gameTeams;
        this.objects = objects;
        this.events = events;
        this.updateTiles = updateTiles;
        this.fillTiles = fillTiles;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.objects.size()); // TODO: Handle more than just objects events (power ups, etc)

        for (GameObject gameObject : this.objects) {
            response.writeInt(gameObject.getGameObjectType().getObjectId());
            gameObject.serialiseObject(response);
        }

        /*for (GamePlayer gamePlayer : this.objects) {
            response.writeInt(0); // type, 0 = player
            response.writeInt(gamePlayer.getPlayer().getRoomUser().getInstanceId());
            response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getX());
            response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getY());
            response.writeInt((int) gamePlayer.getPlayer().getRoomUser().getPosition().getZ());
            response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getRotation());
            response.writeInt(0);
            response.writeInt(-1);
        }*/

        response.writeInt(this.updateTiles.size());

        for (BattleballTile tile : this.updateTiles) {
            response.writeInt(tile.getPosition().getX());
            response.writeInt(tile.getPosition().getY());
            response.writeInt(tile.getColour().getColourId());
            response.writeInt(tile.getState().getTileStateId());
        }

        response.writeInt(this.fillTiles.size());

        for (BattleballTile tile : this.fillTiles) {
            response.writeInt(tile.getPosition().getX());
            response.writeInt(tile.getPosition().getY());
            response.writeInt(tile.getColour().getColourId());
            response.writeInt(tile.getState().getTileStateId());
        }

        response.writeInt(this.gameTeams.size());

        for (GameTeam team : this.gameTeams) {
            response.writeInt(team.getScore());
        }

        response.writeInt(1);
        response.writeInt(this.events.size());

        for (GameEvent gameEvent : this.events) {
            response.writeInt(gameEvent.getGameEventType().getEventId());
            gameEvent.serialiseEvent(response);
        }
        /*response.writeInt(this.movingPlayers.size());

        for (var kvp : this.movingPlayers.entrySet()) {
            response.writeInt(2);
            response.writeInt(kvp.getKey().getPlayer().getRoomUser().getInstanceId());
            response.writeInt(kvp.getValue().getX());
            response.writeInt(kvp.getValue().getY());
        }*/
    }

    @Override
    public short getHeader() {
        return 244; // "Ct"
    }
}
