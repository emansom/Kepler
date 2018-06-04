package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.Kepler;
import org.alexdev.kepler.game.catalogue.CatalogueManager;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;

public class RefreshCatalogueCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add("fuse_administrator_access");
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

        CatalogueManager.reset();

        player.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, player.getRoomUser().getInstanceId(), "Catalogue refreshed."));
    }

    @Override
    public String getDescription() {
        return "Refresh the catalogue";
    }
}