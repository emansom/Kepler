package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.messages.outgoing.rooms.items.DICE_VALUE;

public class DiceTask implements Runnable {
    private final Item dice;

    public DiceTask(Item dice) {
        this.dice = dice;
    }

    @Override
    public void run() {
        int randomNumber = Integer.parseInt(dice.getCustomData());

        dice.getRoom().send(new DICE_VALUE(dice.getId(), false, randomNumber));

        dice.updateStatus();
        dice.setRequiresUpdate(false);
    }
}
