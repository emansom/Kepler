package org.alexdev.kepler.game.triggers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;

import java.util.Map;

public abstract class GameLobbyTrigger extends GenericTrigger {
    public void onRoomEntry(Entity entity, Room room, Object... customArgs) { }
    public void onRoomLeave(Entity entity, Room room, Object... customArgs) { }

    public abstract void createGame(Player gameCreator, Map<String, Object> gameParameters);
    public abstract GameType getGameType();
}
