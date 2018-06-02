package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.items.DICE_VALUE;
import org.alexdev.kepler.messages.outgoing.rooms.items.UPDATE_ITEM;

public class DiceTask implements Runnable {
    private final Room room;

    public DiceTask(Room room) {
        this.room = room;
    }

    @Override
    public void run() {
        for (Item item : this.room.getItems()) {
            if (!item.hasBehaviour(ItemBehaviour.DICE)) {
                continue;
            }

            processDice(item);
        }
    }

    /**
     * Process dices which were interacted with
     *
     * @param dice the dice being used
     */
    private void processDice(Item dice) {
        if (!dice.getRequiresUpdate()) {
            return;
        }

        int randomNumber = Integer.parseInt(dice.getCustomData());

        this.room.send(new DICE_VALUE(dice.getId(), false, randomNumber));

        dice.updateStatus();
        dice.setRequiresUpdate(false);
    }
}
