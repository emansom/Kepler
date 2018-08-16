package org.alexdev.kepler.game.triggers.rooms;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.entities.RoomEntity;
import org.alexdev.kepler.game.triggers.GenericTrigger;
import org.alexdev.kepler.messages.outgoing.rooms.items.SHOWPROGRAM;

import java.util.concurrent.TimeUnit;

public class RooftopRumbleTrigger extends GenericTrigger {
    @Override
    public void onRoomEntry(Player player, Room room, Object... customArgs) {
        if (room.getTaskManager().hasTask("DivingCamera")) {
            DivingDeckTrigger.PoolCamera task = (DivingDeckTrigger.PoolCamera) room.getTaskManager().getTask("DivingCamera");
            player.send(new SHOWPROGRAM(new String[]{"cam1", "targetcamera", String.valueOf(task.getPlayer().getRoomUser().getInstanceId())}));
            player.send(new SHOWPROGRAM(new String[]{"cam1", "setcamera", String.valueOf(task.getCameraType())}));
            return;
        }

        room.getTaskManager().scheduleTask("DivingCamera", new DivingDeckTrigger.PoolCamera(room), 0, 8, TimeUnit.SECONDS);
    }

    @Override
    public void onRoomLeave(Player player, Room room, Object... customArgs)  {

    }
}
