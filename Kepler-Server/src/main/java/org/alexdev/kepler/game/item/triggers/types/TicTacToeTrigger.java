package org.alexdev.kepler.game.item.triggers.types;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.games.GameTicTacToe;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.room.RoomUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicTacToeTrigger extends GameTrigger {
    public TicTacToeTrigger(int roomId) {
        for (var kvp : this.getChairPairs()) {
            this.gameInstances.add(new GameTicTacToe(roomId, kvp));
        }
    }

    @Override
    public void onEntityStep(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        super.onEntityStep(entity, roomUser, item, customArgs);
    }

    @Override
    public void onEntityStop(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        super.onEntityStop(entity, roomUser, item, customArgs);
    }

    @Override
    public void onEntityLeave(Entity entity, RoomUser roomUser, Item item, Object... customArgs) {
        super.onEntityLeave(entity, roomUser, item, customArgs);
    }

    /**
     * Gets the list of seats and their pairs as coordinates
     *
     * @return the map of chair pairs
     */
    @Override
    public List<List<int[]>> getChairPairs() {
        return new ArrayList<>() {{
           add(new ArrayList<>() {{
               add(new int[] { 15, 4});
               add(new int[] { 15, 5});
           }});

            add(new ArrayList<>() {{
                add(new int[] { 15, 9});
                add(new int[] { 15, 10});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 15, 14});
                add(new int[] { 15, 15});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 10, 4});
                add(new int[] { 10, 5});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 10, 9});
                add(new int[] { 10,10});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 10, 14});
                add(new int[] { 10, 15});
            }});


            add(new ArrayList<>() {{
                add(new int[] { 5, 4});
                add(new int[] { 5, 5});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 5, 9});
                add(new int[] { 5, 10});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 5, 14});
                add(new int[] { 5, 15});
            }});
        }};
    }
}
