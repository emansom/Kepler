package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.catalogue.CatalogueManager;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.ItemManager;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.kepler.util.config.GameConfiguration;

public class RefreshSettingsCommand extends Command {
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

        GameConfiguration.reset();
        
        player.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, player.getRoomUser().getInstanceId(), "Game settings refreshed."));
    }

    @Override
    public String getDescription() {
        return "Refresh the game settings";
    }
}