package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.kepler.messages.outgoing.user.ALERT;

import java.util.ArrayList;
import java.util.List;

public class PickupCommand extends Command {
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

        if (!player.getRoomUser().getRoom().isOwner(player.getEntityId())) {
            return;
        }

        List<Item> itemsToPickup = new ArrayList<>();

        for (Item item : player.getRoomUser().getRoom().getItems()) {
            if (item.hasBehaviour(ItemBehaviour.PUBLIC_SPACE_OBJECT)) {
                continue; // Cannot pick up public room furniture.
            }

            itemsToPickup.add(item);
        }

        for (Item item : itemsToPickup) {
            item.setOwnerId(player.getEntityId());
            player.getRoomUser().getRoom().getMapping().removeItem(item);
            player.getInventory().getItems().add(item);
        }

        player.getInventory().getView("last");
        player.send(new ALERT("All furniture items have been picked up"));
    }

    @Override
    public String getDescription() {
        return "Allows the owner to pick up all furniture in a room";
    }
}
