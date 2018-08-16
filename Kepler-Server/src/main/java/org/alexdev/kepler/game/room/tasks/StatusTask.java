package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUserStatus;
import org.alexdev.kepler.game.room.public_rooms.PoolHandler;
import org.alexdev.kepler.messages.outgoing.rooms.user.TYPING_STATUS;
import org.alexdev.kepler.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class StatusTask implements Runnable {
    private final Room room;

    public StatusTask(Room room) {
        this.room = room;
    }

    @Override
    public void run() {
        if (this.room.getEntities().size() == 0) {
            return;
        }

        for (Entity entity : this.room.getEntities()) {
            if (entity != null) {
                if (entity.getRoomUser().getRoom() != null && entity.getRoomUser().getRoom() == this.room) {
                    this.processEntity(entity);
                }
            }
        }
    }

    /**
     * Process entity.
     *
     * @param entity the entity
     */
    private void processEntity(Entity entity) {
        List<String> toRemove = new ArrayList<>();

        if (entity.getType() == EntityType.PLAYER) {
            Player player = (Player) entity;

            this.processHeadRotation(player);
            this.processChatBubble(player);
        }

        for (var kvp : entity.getRoomUser().getStatuses().entrySet()) {
            String key = kvp.getKey();
            RoomUserStatus rus = kvp.getValue();

            if (rus.getActionSwitchCountdown() > 0) {
                rus.setActionSwitchCountdown(rus.getActionSwitchCountdown() - 1);
            } else if (rus.getActionSwitchCountdown() == 0) {
                rus.setActionSwitchCountdown(-1);
                rus.setActionCountdown(rus.getSecActionSwitch());

                // Swap back to original key and update status
                rus.swapKeyAction();
                entity.getRoomUser().setNeedsUpdate(true);
            }

            if (rus.getActionCountdown() > 0) {
                rus.setActionCountdown(rus.getActionCountdown() - 1);
            } else if (rus.getActionCountdown() == 0) {
                rus.setActionCountdown(-1);
                rus.setActionSwitchCountdown(rus.getSecSwitchLifetime());

                // Swap key to action and update status
                rus.swapKeyAction();
                entity.getRoomUser().setNeedsUpdate(true);
            }

            if (rus.getLifetimeCountdown() > 0) {
                rus.setLifetimeCountdown(rus.getLifetimeCountdown() - 1);
            } else if (rus.getLifetimeCountdown() == 0) {
                rus.setLifetimeCountdown(-1);
                toRemove.add(key);

                entity.getRoomUser().setNeedsUpdate(true);
            }
        }

        for (String keyRemove : toRemove) {
            entity.getRoomUser().getStatuses().remove(keyRemove);
        }
    }

    private void processChatBubble(Player entity) {
        if (entity.getRoomUser().getTimerManager().getChatBubbleTimer() != -1 &&
                DateUtil.getCurrentTimeSeconds() > entity.getRoomUser().getTimerManager().getChatBubbleTimer()) {

            entity.getRoomUser().setTyping(false);
            entity.getRoomUser().getTimerManager().stopChatBubbleTimer();
            entity.getRoomUser().getRoom().send(new TYPING_STATUS(entity.getRoomUser().getInstanceId(), false));
        }
    }


    private void processHeadRotation(Player entity) {
        if (entity.getRoomUser().getTimerManager().getLookTimer() != -1 &&
                DateUtil.getCurrentTimeSeconds() > entity.getRoomUser().getTimerManager().getLookTimer()) {

            entity.getRoomUser().getTimerManager().stopLookTimer();
            entity.getRoomUser().getPosition().setHeadRotation(entity.getRoomUser().getPosition().getBodyRotation());
            entity.getRoomUser().setNeedsUpdate(true);
        }
    }
}
