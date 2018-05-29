package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.util.StringUtil;

public class SitCommand extends Command {

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

        if (player.getRoom() == null) {
            return;
        }

        if (player.getRoomUser().containsStatus(StatusType.SIT)) {
            return;
        }

        int x = player.getRoomUser().getPosition().getX();
        int y = player.getRoomUser().getPosition().getY();
        double itemHeight = 0.0;
        double tileHeight = player.getRoom().getModel().getTileHeight(x, y);
        int rotation = player.getRoomUser().getPosition().getRotation() / 2 * 2;

        RoomTile tile = player.getRoomUser().getTile();

        if (tile.getHighestItem() != null) {
            Item item = tile.getHighestItem();

            itemHeight = item.getDefinition().getTopHeight();
            rotation = item.getPosition().getRotation();
        }

        player.getRoomUser().getPosition().setZ(tileHeight);
        player.getRoomUser().getPosition().setRotation(rotation);
        player.getRoomUser().setStatus(StatusType.SIT, StringUtil.format(itemHeight));
        player.getRoomUser().removeStatus(StatusType.DANCE);
        player.getRoomUser().setNeedsUpdate(true);
    }

    @Override
    public String getDescription() {
        return "Puts your arse on the floor.";
    }
}
