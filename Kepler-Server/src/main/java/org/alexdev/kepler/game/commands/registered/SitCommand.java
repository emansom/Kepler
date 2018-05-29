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

        RoomTile tile = player.getRoomUser().getTile();
        double height = tile.getTileHeight();

        if (height != player.getRoomUser().getPosition().getZ()) {
            player.getRoomUser().getPosition().setZ(height);
        }

        Item item = null;

        if (tile.getHighestItem() != null) {
            item = tile.getHighestItem();
        }

        if (item != null) {
            if (!item.getBehaviour().isCanSitOnTop()) {
                return;
            }

            height = item.getDefinition().getTopHeight();
            player.getRoomUser().getPosition().setRotation(item.getPosition().getRotation());
            player.getRoomUser().setStatus(StatusType.SIT, StringUtil.format(item.getDefinition().getTopHeight()));
        } else {
            int rotation = player.getRoomUser().getPosition().getRotation();

            if (rotation > 0 && rotation <= 7) {
                if (rotation % 2 == 0) {
                    rotation--;
                }
            }

            player.getRoomUser().getPosition().setRotation(rotation);
            player.getRoomUser().setStatus(StatusType.SIT, StringUtil.format(tile.getTileHeight()));
        }

        player.getRoomUser().removeStatus(StatusType.DANCE);
        player.getRoomUser().setNeedsUpdate(true);
    }

    @Override
    public String getDescription() {
        return "Puts your arse on the floor.";
    }
}
