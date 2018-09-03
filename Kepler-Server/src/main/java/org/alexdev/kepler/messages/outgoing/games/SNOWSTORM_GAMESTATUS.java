package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.games.snowstorm.SnowStormGame;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.Collection;
import java.util.List;

public class SNOWSTORM_GAMESTATUS extends MessageComposer {
    private final SnowStormGame game;
    private final List<GameObject> objects;
    private final GamePlayer gamePlayer;

    public SNOWSTORM_GAMESTATUS(SnowStormGame game, List<GameObject> objects, GamePlayer gamePlayer) {
        this.game = game;
        this.objects = objects;
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.gamePlayer.getTurnContainer().getCurrentTurn().get());
        response.writeInt(this.gamePlayer.getTurnContainer().getCheckSum());
        response.writeInt(1);

        response.writeInt(this.objects.size());

        for (GameObject gameObject : this.objects) {
            response.writeInt(gameObject.getGameObjectType().getObjectId());
            gameObject.serialiseObject(response);
        }
    }

    @Override
    public short getHeader() {
        return 244; // "Ct"
    }
}
