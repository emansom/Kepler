package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.commands.CommandManager;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.user.ALERT;

public class HelpCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add("default");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        StringBuilder about = new StringBuilder();
        about.append("Commands ('<' and '>' are optional parameters):<br>");

        for (var set : CommandManager.getInstance().getCommands().entrySet()) {
            if (!CommandManager.getInstance().hasCommandPermission(entity, set.getValue())) {
                continue;
            }

            Command command = set.getValue();
            about.append(":").append(String.join("/", set.getKey()));

            if (command.getArguments().length > 0) {
                if (command.getArguments().length > 1) {
                    about.append(" [").append(String.join("] [", command.getArguments())).append("]");
                } else {
                    about.append(" [").append(command.getArguments()[0]).append("]");
                }
            }

            about.append(" - ").append(command.getDescription()).append("<br>");
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.send(new ALERT(about.toString()));
        }
    }

    @Override
    public String getDescription() {
        return "List available commands";
    }
}
