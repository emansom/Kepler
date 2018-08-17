package org.alexdev.kepler.game.room.triggers;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.triggers.GenericTrigger;

public class HabboLidoTrigger extends GenericTrigger {
    @Override
    public void onRoomEntry(Player player, Room room, Object... customArgs) {
        if (player.getRoomUser().getPosition().getZ() == 1.0) { // User entered room from the other pool
            player.getRoomUser().setStatus(StatusType.SWIM, "");
            player.getRoomUser().setNeedsUpdate(true);
        }
    }

    @Override
    public void onRoomLeave(Player player, Room room, Object... customArgs)  {

    }
}
