package org.alexdev.kepler.game.room.models.triggers;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.triggers.GenericTrigger;
import org.alexdev.kepler.messages.outgoing.games.LOUNGEINFO;

public class BattleballLobbyTrigger extends GenericTrigger {
    @Override
    public void onRoomEntry(Player player, Room room, Object... customArgs) {
        player.send(new LOUNGEINFO());
    }

    @Override
    public void onRoomLeave(Player player, Room room, Object... customArgs) {

    }
}
