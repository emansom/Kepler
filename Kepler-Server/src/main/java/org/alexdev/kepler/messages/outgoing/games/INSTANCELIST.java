package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.utils.FinishedGame;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.enums.GameState;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;
import java.util.stream.Collectors;

public class INSTANCELIST extends MessageComposer {
    private final List<Game> createdGames;
    private final List<Game> startedGames;
    private final List<FinishedGame> finishedGames;

    public INSTANCELIST(List<Game> gamesByType, List<FinishedGame> finishedGames) {
        this.createdGames = gamesByType.stream().filter(game -> game.getGameState() == GameState.WAITING).collect(Collectors.toList());
        this.startedGames = gamesByType.stream().filter(game -> game.getGameState() == GameState.STARTED).collect(Collectors.toList());
        this.finishedGames = finishedGames;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.createdGames.size());

        for (Game game : this.createdGames) {
            response.writeInt(game.getId());
            response.writeString(game.getName());

            response.writeInt(game.getGameCreatorId());
            response.writeString(game.getGameCreator());

            response.writeInt(game.getMapId());
        }

        response.writeInt(this.startedGames.size());

        for (Game game : this.startedGames) {
            response.writeInt(game.getId());
            response.writeString(game.getName());
            response.writeString(game.getGameCreator());
            response.writeInt(game.getMapId());
        }

        response.writeInt(this.finishedGames.size());

        for (FinishedGame game : this.finishedGames) {
            response.writeInt(game.getId());
            response.writeString(game.getName());
            response.writeString(game.getMapCreator());
            response.writeInt(game.getMapId());
        }
        /*response.writeInt(this.createdGames.size());

        for (Game game : this.createdGames) {
            response.writeInt(game.getId());
            response.writeString(game.getName());
            response.writeInt(game.getGameCreator().getRoomUser().getInstanceId());
            response.writeString(game.getGameCreator().getDetails().getName());
            response.writeInt(0); // Number of spectators

            response.writeInt(game.getTeamAmount());

            for (int i = 0; i < game.getTeamAmount(); i++) {
                response.writeInt(game.getTeams().get(i).size());

                for (GamePlayer gamePlayer : game.getTeams().get(i)) {
                    response.writeInt(gamePlayer.getPlayer().getRoomUser().getInstanceId());
                    response.writeString(gamePlayer.getPlayer().getDetails().getName());
                }
            }
        }

        response.writeInt(this.startedGames.size());

        for (Game game : this.startedGames) {
            response.writeInt(game.getId());
            response.writeString(game.getName());
            response.writeString(game.getGameCreator().getDetails().getName());

            response.writeInt(game.getTeamAmount());

            for (int i = 0; i < game.getTeamAmount(); i++) {
                response.writeInt(game.getTeams().get(i).size());

                for (GamePlayer gamePlayer : game.getTeams().get(i)) {
                    response.writeString(gamePlayer.getPlayer().getDetails().getName());
                }
            }
        }

        response.writeInt(this.finishedGames.size());

        for (Game game : this.finishedGames) {
            response.writeInt(game.getId());
            response.writeString(game.getName());
            response.writeString(game.getGameCreator().getDetails().getName());

            response.writeInt(game.getTeamAmount());

            for (int i = 0; i < game.getTeamAmount(); i++) {
                response.writeInt(game.getTeams().get(i).size());
                int teamScore = 0;

                for (GamePlayer gamePlayer : game.getTeams().get(i)) {
                    response.writeString(gamePlayer.getPlayer().getDetails().getName());
                    response.writeInt(gamePlayer.getScore());

                    teamScore += gamePlayer.getScore();
                }

                response.writeInt(teamScore);
            }
        }*/
    }

    @Override
    public short getHeader() {
        return 232; // "Ch"
    }
}
