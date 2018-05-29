package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.RoomUserStatus;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.kepler.messages.outgoing.user.ALERT;

public class GiveDrinkCommand extends Command {

    @Override
    public void addPermissions() {
        this.permissions.add("default");
    }

    @Override
    public void addArguments() {
        this.arguments.add("user");
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

        Player targetUser = PlayerManager.getInstance().getPlayerByName(args[0]);

        if (targetUser == null ||
                targetUser.getRoomUser().getRoom() == null ||
                targetUser.getRoom().getId() != player.getRoom().getId()) {
            player.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, player.getRoomUser().getInstanceId(), "Could not find user: " + args[0]));
            return;
        }

        if (!player.getRoomUser().containsStatus(StatusType.CARRY_DRINK) && !player.getRoomUser().containsStatus(StatusType.CARRY_FOOD)) {
            player.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, player.getRoomUser().getInstanceId(), "You are not carrying any food or drinks to give."));
            return;
        }

        RoomUserStatus status = null;

        if (player.getRoomUser().containsStatus(StatusType.CARRY_DRINK)) {
            status = player.getRoomUser().getStatuses().get(StatusType.CARRY_DRINK.getStatusCode());
        }

        if (player.getRoomUser().containsStatus(StatusType.CARRY_FOOD)) {
            status = player.getRoomUser().getStatuses().get(StatusType.CARRY_FOOD.getStatusCode());
        }

        if (status != null) {
            // Give drink to user if they're not already having a drink or food, and they're not dancing
            if (!targetUser.getRoomUser().containsStatus(StatusType.CARRY_FOOD) &&
                !targetUser.getRoomUser().containsStatus(StatusType.CARRY_DRINK)) {
                if (!targetUser.getRoomUser().containsStatus(StatusType.DANCE)) {
                    targetUser.getRoomUser().carryItem(Integer.parseInt(status.getValue()), null);

                    targetUser.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, targetUser.getRoomUser().getInstanceId(), player.getDetails().getName() + " handed you their drink."));

                    player.getRoomUser().removeStatus(StatusType.CARRY_DRINK);
                    player.getRoomUser().removeStatus(StatusType.CARRY_FOOD);
                    player.getRoomUser().setNeedsUpdate(true);
                } else {
                    player.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, player.getRoomUser().getInstanceId(), targetUser.getDetails().getName() + " is dancing."));
                }
            } else {
                player.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, player.getRoomUser().getInstanceId(), targetUser.getDetails().getName() + " is already enjoying a drink."));
            }
        }
    }

    @Override
    public String getDescription() {
        return "Gives a user your own drink.";
    }
}
