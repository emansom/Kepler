package org.alexdev.kepler.game.triggers.rooms;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.triggers.GenericTrigger;
import org.alexdev.kepler.messages.outgoing.rooms.items.SHOWPROGRAM;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class HabboLidoTrigger extends GenericTrigger {
    @Override
    public void onRoomEntry(Player player, Room room, Object... customArgs) {

    }

    @Override
    public void onRoomLeave(Player player, Room room, Object... customArgs)  {

    }
}
