package org.alexdev.kepler.game.commands.registered;


import org.alexdev.kepler.Kepler;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE.ChatMessageType;
import org.alexdev.kepler.messages.outgoing.user.ALERT;
import org.alexdev.kepler.util.config.GameConfiguration;

import java.time.Duration;

public class ShutdownCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add("fuse_administrator_access");
    }

    @Override
    public void addArguments() {
        this.arguments.add("minutes");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        // Abort maintenance shutdown if provided argument is either cancel, off or stop (case insensitive)
        if (args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("stop")) {
            PlayerManager.getInstance().cancelMaintenanceShutdown();
            player.send(new CHAT_MESSAGE(ChatMessageType.WHISPER, player.getRoomUser().getInstanceId(), "Cancelled shutdown"));
            return;
        }

        long minutes;

        // Try parsing minutes argument, use default if failed
        try {
            minutes = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            minutes = GameConfiguration.getInstance().getLong("shutdown.minutes");
            player.send(new CHAT_MESSAGE(ChatMessageType.WHISPER, player.getRoomUser().getInstanceId(), "Failed to parse minutes provided to shutdown command, defaulting to " + minutes + " minute(s)"));
        }

        // Enqueue maintenance shutdown
        PlayerManager.getInstance().enqueueMaintenanceShutdown(Duration.ofMinutes(minutes));

        // Let callee know Kepler is shutting down in X minutes
        player.send(new CHAT_MESSAGE(ChatMessageType.WHISPER, player.getRoomUser().getInstanceId(), "Shutting down in " + minutes + " minute(s)"));
    }

    @Override
    public String getDescription() {
        return "Shutdown Kepler";
    }
}