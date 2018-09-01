package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.ArrayList;
import java.util.List;

public class FULLGAMESTATUS extends MessageComposer {
    private final Game game;
    private final List<GamePlayer> gamePlayerList;
    private final boolean startedGame;

    public FULLGAMESTATUS(Game game, boolean startedGame) {
        this.game = game;
        this.gamePlayerList = new ArrayList<>();
        this.startedGame = startedGame;

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
                /*} else {
                    response.writeInt(0); // type, 0 = player
                    response.writeInt(gamePlayer.getPlayer().getDetails().getId());
                    response.writeInt(gamePlayer.getPosition().getX());
                    response.writeInt(gamePlayer.getPosition().getY());
                    response.writeInt((int) gamePlayer.getPosition().getZ());
                    response.writeInt(gamePlayer.getPosition().getRotation());
                    response.writeInt(0);
                    response.writeInt(-1);
                    response.writeString(gamePlayer.getPlayer().getDetails().getName());
                    response.writeString(gamePlayer.getPlayer().getDetails().getMotto());
                    response.writeString(gamePlayer.getPlayer().getDetails().getFigure());
                    response.writeString(gamePlayer.getPlayer().getDetails().getSex());
                    response.writeInt(gamePlayer.getTeamId());
                    response.writeInt(gamePlayer.getPlayer().getDetails().getId());
                }*/
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
        response.writeInt(0);
    }

    @Override
    public short getHeader() {
        return 243; // "Cs"
    }
}
