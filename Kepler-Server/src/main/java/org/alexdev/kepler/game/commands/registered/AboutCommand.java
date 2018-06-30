package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.user.ALERT;

public class AboutCommand extends Command {

    @Override
    public void addPermissions() {
        this.permissions.add("default");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player)entity;
        player.send(new ALERT("Kepler server\n\nContributors:\n - Hoshiko\n - Ascii\n - Leon\n - Romuald\n - Glaceon\n\nMade by Quackster"));
    }

    @Override
    public String getDescription() {
        return "Information about the software powering this retro";
    }
}
