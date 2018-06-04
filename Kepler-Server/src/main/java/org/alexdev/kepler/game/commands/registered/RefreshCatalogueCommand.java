package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.catalogue.CatalogueManager;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.ItemManager;
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

        // To avoid confusion we also reload the item_definitions table, as those have likely been edited
        ItemManager.reset();

        // Reload the catalogue manager (catalogue_pages, catalogue_packages and catalogue_items tables)
        CatalogueManager.reset();


        // TODO: calculate diff between previous calatogue and current and whisper summary of changes

        player.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, player.getRoomUser().getInstanceId(), "Catalogue refreshed."));
    }

    @Override
    public String getDescription() {
        return "Refresh the catalogue";
    }
}