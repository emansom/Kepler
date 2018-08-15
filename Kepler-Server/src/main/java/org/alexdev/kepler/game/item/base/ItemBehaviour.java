package org.alexdev.kepler.game.item.base;

import org.alexdev.kepler.game.item.triggers.ItemTrigger;
import org.alexdev.kepler.game.item.triggers.generic.BedTrigger;
import org.alexdev.kepler.game.item.triggers.generic.ChairTrigger;

public enum ItemBehaviour {
    SOLID,
    CAN_STACK_ON_TOP,
    CAN_SIT_ON_TOP(new ChairTrigger()),
    CAN_STAND_ON_TOP,
    CAN_LAY_ON_TOP(new BedTrigger()),
    CUSTOM_DATA_NUMERIC_ON_OFF,
    REQUIRES_TOUCHING_FOR_INTERACTION,
    CUSTOM_DATA_TRUE_FALSE,
    PUBLIC_SPACE_OBJECT,
    EXTRA_PARAMETER,
    DICE,
    CUSTOM_DATA_ON_OFF,
    CUSTOM_DATA_NUMERIC_STATE,
    TELEPORTER,
    REQUIRES_RIGHTS_FOR_INTERACTION,
    DOOR,
    PRIZE_TROPHY,
    ROLLER,
    REDEEMABLE,
    SOUND_MACHINE,
    SOUND_MACHINE_SAMPLE_SET,
    JUKEBOX,
    WALL_ITEM,
    POST_IT,
    DECORATION,
    WHEEL_OF_FORTUNE,
    ROOMDIMMER,
    PRESENT,
    PHOTO;


    private ItemTrigger trigger;

    ItemBehaviour() {
        trigger = null;
    }
    ItemBehaviour(ItemTrigger trigger) {
        this.trigger = trigger;
    }

    public ItemTrigger getTrigger() {
        return trigger;
    }
}
