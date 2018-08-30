package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.FinishedGame;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameState;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class GAMEINSTANCE extends MessageComposer {
    private Game game;
    private FinishedGame finishedGame;

    public GAMEINSTANCE(Game game) {
        this.game = game;
    }

    public GAMEINSTANCE(FinishedGame game) {
        this.finishedGame = game;
    }

    @Override
    public void compose(NettyResponse response) {
        if (this.finishedGame == null) {
            response.writeInt(this.game.getGameState().getStateId());

            if (this.game.getGameState() == GameState.WAITING) {
                response.writeInt(this.game.getId());
                response.writeString(this.game.getName());

                response.writeInt(this.game.getGameCreator().getRoomUser().getInstanceId());
                response.writeString(this.game.getGameCreator().getDetails().getName());

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

            if (this.game.getGameState() == GameState.STARTED) {
                response.writeInt(this.game.getId());
                response.writeString(this.game.getName());
                response.writeString(this.game.getGameCreator().getDetails().getName());
                response.writeInt(this.game.getMapId());
                response.writeInt(this.game.getTeamAmount());

                for (int i = 0; i < this.game.getTeamAmount(); i++) {
                    List<GamePlayer> playerList = this.game.getTeams().get(i).getPlayers();

                    response.writeInt(playerList.size());

                    for (GamePlayer player : playerList) {
                        //response.writeInt(player.getPlayer().getRoomUser().getInstanceId());
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
        } else {
            response.writeInt(GameState.ENDED.getStateId());
            response.writeInt(this.finishedGame.getId());
            response.writeString(this.finishedGame.getName());
            response.writeString(this.finishedGame.getMapCreator());
            response.writeInt(this.finishedGame.getMapId());
            response.writeInt(this.finishedGame.getTeamScores().size());

            for (int i = 0; i < this.finishedGame.getTeamScores().size(); i++) {
                FinishedGame.FinishedGameTeam gameTeam = this.finishedGame.getTeamScores().get(i);
                response.writeInt(gameTeam.getPlayerScores().size());

                for (var kvp : gameTeam.getPlayerScores()) {
                    response.writeString(kvp.getKey());
                    response.writeInt(kvp.getValue());
                }

                response.writeInt(gameTeam.getScore());
            }

            // TODO: Special SnowStorm parameters

            String[] powerUps = new String[this.finishedGame.getPowerUps().size()];

            for (int i = 0; i < this.finishedGame.getPowerUps().size(); i++) {
                powerUps[i] = String.valueOf(this.finishedGame.getPowerUps().get(i));
            }

            response.writeString(String.join(",", powerUps));
        }
    }

    @Override
    public short getHeader() {
        return 233; // "Ci"
    }
}
