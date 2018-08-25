package org.alexdev.kepler.game.triggers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.games.GameType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.entities.RoomEntity;

public abstract class GameLobbyTrigger extends GenericTrigger {
    public void onRoomEntry(Entity entity, Room room, Object... customArgs) { }
    public void onRoomLeave(Entity entity, Room room, Object... customArgs) { }

    public abstract GameType getGameType();
}
