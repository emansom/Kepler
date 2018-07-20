package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.messages.outgoing.user.ALERT;

import java.util.ArrayList;
import java.util.List;

public class AfkCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add("default");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        if (!player.getRoomUser().containsStatus(StatusType.SLEEP)) {
            player.getRoomUser().setStatus(StatusType.SLEEP, "");
            player.getRoomUser().setNeedsUpdate(true);
        }
    }

    @Override
    public String getDescription() {
        return "Put your eyes to sleep";
    }
}
