package org.alexdev.kepler.game.item.triggers.types;

import org.alexdev.kepler.dao.mysql.CurrencyDao;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.ItemTrigger;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.messages.outgoing.rooms.pool.JUMPINGPLACE_OK;
import org.alexdev.kepler.messages.outgoing.user.currencies.TICKET_BALANCE;

public class PoolLiftTrigger implements ItemTrigger {
    @Override
    public void onEntityStep(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {

    }

    @Override
    public void onEntityStop(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player)entity;
        item.showProgram("close");

        player.getRoomUser().setWalkingAllowed(false);
        player.getRoomUser().getTimerManager().resetRoomTimer(120); // Only allow 120 seconds when diving, to stop the queue from piling up if someone goes AFK.
        player.getRoomUser().setDiving(true);

        CurrencyDao.decreaseTickets(player.getDetails(), 1);

        player.send(new TICKET_BALANCE(player.getDetails().getTickets()));
        player.send(new JUMPINGPLACE_OK());
    }
}
