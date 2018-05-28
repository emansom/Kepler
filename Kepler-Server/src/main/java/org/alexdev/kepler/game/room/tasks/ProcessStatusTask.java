package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUserStatus;
import org.alexdev.kepler.game.room.public_rooms.PoolHandler;
import org.alexdev.kepler.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class ProcessStatusTask implements Runnable {
    private final Room room;

    public ProcessStatusTask(Room room) {
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

        if (entity.getRoomUser().getLookTimer() != -1 && DateUtil.getCurrentTimeSeconds() > entity.getRoomUser().getLookTimer()) {
            entity.getRoomUser().setLookTimer(-1);
            entity.getRoomUser().getPosition().setHeadRotation(entity.getRoomUser().getPosition().getBodyRotation());
            entity.getRoomUser().setNeedsUpdate(true);
        }

        // Use walk to next tile if on pool queue
        PoolHandler.checkPoolQueue(entity);

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

        for (String keyRemove: toRemove) {
            entity.getRoomUser().getStatuses().remove(keyRemove);
        }
    }
}
