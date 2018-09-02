package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.games.snowstorm.SnowStormGame;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.Collection;
import java.util.List;

public class SNOWSTORM_GAMESTATUS extends MessageComposer {
    private final Game game;

    private final Collection<GameTeam> gameTeams;

    private final List<GameObject> objects;
    private final List<GameEvent> events;

    public SNOWSTORM_GAMESTATUS(SnowStormGame game, Collection<GameTeam> gameTeams, List<GameObject> objects, List<GameEvent> events) {
        this.game = game;
        this.gameTeams = gameTeams;
        this.objects = objects;
        this.events = events;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.objects.size()); // TODO: Handle more than just objects events (power ups, etc)

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
