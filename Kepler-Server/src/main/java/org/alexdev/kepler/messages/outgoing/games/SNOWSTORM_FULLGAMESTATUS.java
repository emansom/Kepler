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

public class SNOWSTORM_FULLGAMESTATUS extends MessageComposer {
    private final Game game;
    private final List<GamePlayer> gamePlayerList;
    private final GamePlayer gamePlayer;

    public SNOWSTORM_FULLGAMESTATUS(Game game, GamePlayer gamePlayer) {
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
        response.writeInt(this.gamePlayerList.size());

        List<GameObject> objects = new ArrayList<>();

        for (var gamePlayer : this.gamePlayerList) {
            objects.add(new SnowStormPlayerObject(gamePlayer));
        }

        for (var obj : objects) {
            obj.serialiseObject(response);
        }

        response.writeBool(false);
        response.writeInt(this.game.getTeamAmount());

        this.gamePlayer.getTurnContainer().getCurrentTurn().incrementAndGet();
        this.gamePlayer.getTurnContainer().calculateChecksum(objects);

        new SNOWSTORM_GAMESTATUS((SnowStormGame) this.game, List.of(), this.gamePlayer).compose(response);
    }

    @Override
    public short getHeader() {
        return 243; // "Cs"
    }
}
