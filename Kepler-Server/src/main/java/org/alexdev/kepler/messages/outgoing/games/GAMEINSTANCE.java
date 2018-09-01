package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.utils.FinishedGame;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.enums.GameState;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
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

                response.writeInt(this.game.getGameCreatorId());
                response.writeString(this.game.getGameCreator());

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

                if (this.game.getGameType() == GameType.BATTLEBALL) {
                    BattleballGame battleballGame = (BattleballGame) this.game;
                    int[] allowedPowerUps = battleballGame.getAllowedPowerUps();

                    String[] powerUps = new String[allowedPowerUps.length];

                    for (int i = 0; i < allowedPowerUps.length; i++) {
                        powerUps[i] = String.valueOf(allowedPowerUps[i]);
                    }

                    response.writeString(String.join(",", powerUps));
                }
            }

            if (this.game.getGameState() == GameState.STARTED) {
                response.writeInt(this.game.getId());
                response.writeString(this.game.getName());
                response.writeString(this.game.getGameCreator());
                response.writeInt(this.game.getMapId());
                response.writeInt(this.game.getTeamAmount());

                for (int i = 0; i < this.game.getTeamAmount(); i++) {
                    List<GamePlayer> playerList = this.game.getTeams().get(i).getActivePlayers();

                    response.writeInt(playerList.size());

                    for (GamePlayer player : playerList) {
                        //response.writeInt(player.getPlayer().getRoomUser().getInstanceId());
                        response.writeString(player.getPlayer().getDetails().getName());
                    }
                }

                // TODO: Special SnowStorm parameters

                if (this.game.getGameType() == GameType.BATTLEBALL) {
                    BattleballGame battleballGame = (BattleballGame) this.game;
                    int[] allowedPowerUps = battleballGame.getAllowedPowerUps();

                    String[] powerUps = new String[allowedPowerUps.length];

                    for (int i = 0; i < allowedPowerUps.length; i++) {
                        powerUps[i] = String.valueOf(allowedPowerUps[i]);
                    }

                    response.writeString(String.join(",", powerUps));
                }

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

            int[] allowedPowerUps = this.finishedGame.getPowerUps();
            String[] powerUps = new String[allowedPowerUps.length];

            for (int i = 0; i < allowedPowerUps.length; i++) {
                powerUps[i] = String.valueOf(allowedPowerUps[i]);
            }

            response.writeString(String.join(",", powerUps));
        }
    }

    @Override
    public short getHeader() {
        return 233; // "Ci"
    }
}
