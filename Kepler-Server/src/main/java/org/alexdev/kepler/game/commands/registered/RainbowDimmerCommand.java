package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.tasks.RainbowTask;
import org.alexdev.kepler.messages.outgoing.user.ALERT;

import java.util.concurrent.TimeUnit;

public class RainbowDimmerCommand extends Command {
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

        Room room = player.getRoom();

        if (!room.isOwner(player.getEntityId())) {
            return;
        }

        Item moodlight = room.getItemManager().getMoodlight();

        if (moodlight == null) {
            player.send(new ALERT("This command requires a roomdimmer placed for it to work"));
            return;
        }

        RainbowTask rainbowTask = new RainbowTask(room);
        room.getTaskManager().scheduleCustomTask("RainbowTask", rainbowTask, 1, TimeUnit.SECONDS);
    }

    @Override
    public String getDescription() {
        return "Cycles through the rainbow in your very own room!";
    }
}
