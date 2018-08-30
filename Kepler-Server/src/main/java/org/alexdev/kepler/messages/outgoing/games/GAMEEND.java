package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.games.GameType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.Map;

public class GAMEEND extends MessageComposer {
    private final Map<Integer, GameTeam> teams;

    public GAMEEND(Map<Integer, GameTeam> teams) {
        this.teams = teams;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(GameManager.getInstance().getRestartSeconds(GameType.BATTLEBALL));
        response.writeInt(this.teams.size());

        for (GameTeam team : this.teams.values()) {
            var players = team.getPlayers();

            if (players.size() > 0) {
                response.writeInt(players.size());

                for (GamePlayer gamePlayer : players) {
                    response.writeInt(gamePlayer.getPlayer().getDetails().getId());
                    response.writeString(gamePlayer.getPlayer().getDetails().getName());
                    response.writeInt(gamePlayer.getScore());
                }

                response.writeInt(team.getScore());
            } else {
                response.writeInt(-1);
            }
        }
    }

    @Override
    public short getHeader() {
        return 248;
    }
}
