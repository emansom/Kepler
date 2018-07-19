package org.alexdev.kepler.game.commands.clientside;

import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;

public class EventsCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add("default");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {

    }

    @Override
    public String getDescription() {
        return "Show current events organised by other users";
    }
}
