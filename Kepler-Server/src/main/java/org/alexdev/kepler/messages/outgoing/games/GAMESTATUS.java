package org.alexdev.kepler.messages.outgoing.games;

import gherkin.lexer.En;
import gherkin.lexer.Pl;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GAMESTATUS extends MessageComposer {
    private final Game game;
    private final Collection<GameTeam> gameTeams;
    private final List<GamePlayer> players;
    private final Map<GamePlayer, Position> movingPlayers;
    private final List<Position> updateTiles;

    public GAMESTATUS(Game game, Collection<GameTeam> gameTeams, List<GamePlayer> players, Map<GamePlayer, Position> movingPlayers, List<Position> updateTiles) {
        this.game = game;
        this.gameTeams = gameTeams;
        this.players = players;
        this.movingPlayers = movingPlayers;
        this.updateTiles = updateTiles;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.players.size());

        for (GamePlayer gamePlayer : this.players) {
            response.writeInt(0); // type, 0 = player
            response.writeInt(gamePlayer.getPlayer().getRoomUser().getInstanceId());
            response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getX());
            response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getY());
            response.writeInt((int) gamePlayer.getPlayer().getRoomUser().getPosition().getZ());
            response.writeInt(gamePlayer.getPlayer().getRoomUser().getPosition().getRotation());
            response.writeInt(0);
            response.writeInt(-1);
        }

        response.writeInt(this.updateTiles.size());

        for (Position tile : this.updateTiles) {
            response.writeInt(tile.getX());
            response.writeInt(tile.getY());
            response.writeInt(this.game.getBattleballTileColours()[tile.getX()][tile.getY()].getTileColourId());
            response.writeInt(this.game.getBattleballTileStates()[tile.getX()][tile.getY()].getTileStateId());
        }

        response.writeInt(0);
        response.writeInt(this.gameTeams.size());

        for (GameTeam team : this.gameTeams) {
            team.setScore(team.getScore() + 1);
            response.writeInt(team.getScore());
        }

        response.writeInt(1);
        response.writeInt(this.movingPlayers.size());

        for (var kvp : this.movingPlayers.entrySet()) {
            response.writeInt(2);
            response.writeInt(kvp.getKey().getPlayer().getRoomUser().getInstanceId());
            response.writeInt(kvp.getValue().getX());
            response.writeInt(kvp.getValue().getY());
        }
    }

    @Override
    public short getHeader() {
        return 244; // "Ct"
    }
}
