package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.Kepler;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.outgoing.user.ALERT;
import org.alexdev.kepler.util.DateUtil;

public class UptimeCommand extends Command {
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

        int authenticatedPlayers = PlayerManager.getInstance().getPlayers().size();
        int activePlayers = PlayerManager.getInstance().getActivePlayers().size();

        long uptime = (DateUtil.getCurrentTimeSeconds() - Kepler.getStartupTime()) * 1000;
        long days = (uptime / (1000 * 60 * 60 * 24));
        long hours = (uptime - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (uptime - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (uptime - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);

        Runtime runtime = Runtime.getRuntime();
        int memoryUsage = (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);

        StringBuilder msg = new StringBuilder();
        msg.append("SERVER\r");
        msg.append("Server uptime is " + days + " day(s), " + hours + " hour(s), " + minutes + " minute(s) and " + seconds + " second(s).\r");
        msg.append("There are " + activePlayers + " active players, and " + authenticatedPlayers + " authenticated players.\r");
        msg.append("\r");
        msg.append("SYSTEM\r");
        msg.append("CPU cores: " + runtime.availableProcessors() + "\r");
        msg.append("JVM memory usage: " + memoryUsage + " MB\r");
        msg.append("Java: " + System.getProperty("java.version") + " " + System.getProperty("java.vendor") + "\r");
        msg.append("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        player.send(new ALERT(msg.toString()));
    }

    @Override
    public String getDescription() {
        return "Get the uptime and status of the server.";
    }
}
