package org.alexdev.kepler.console;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.room.RoomUser;

public class ConsoleOperator extends Entity {

    @Override
    public boolean hasPermission(String permission) {
        return permission.equalsIgnoreCase("operator");
    }

    @Override
    public PlayerDetails getDetails() {
        return null;
    }

    @Override
    public RoomUser getRoomUser() {
        return null;
    }

    @Override
    public EntityType getType() {
        return null;
    }

    @Override
    public void dispose() {

    }
}
