package org.alexdev.kepler.game.item.triggers.types;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.games.GameTicTacToe;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.room.RoomUser;

import java.util.HashMap;
import java.util.Map;

public class TicTacToeTrigger extends GameTrigger {
    public TicTacToeTrigger(int roomId) {
        for (var kvp : this.getChairPairs().entrySet()) {
            int[] chairPosition = kvp.getKey();
            int[] opponentPosition = kvp.getValue();

            this.gameInstances.add(new GameTicTacToe(roomId, chairPosition, opponentPosition));
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
    public Map<int[], int[]> getChairPairs() {
        return new HashMap<>() {{
            put(new int[] { 15, 4}, new int[] { 15, 5});
            put(new int[] { 15, 9}, new int[] { 15, 10});
            put(new int[] { 15, 14}, new int[] { 15, 15});
            put(new int[] { 10, 4}, new int[] { 10, 5});
            put(new int[] { 10, 9}, new int[] { 10, 10});
            put(new int[] { 10, 14}, new int[] { 10, 15});
            put(new int[] { 5, 4}, new int[] { 5, 5});
            put(new int[] { 5, 9}, new int[] { 5, 10});
            put(new int[] { 5, 14}, new int[] { 5, 15});
        }};
    }

    /**
     * Get FUSE game type
     *
     * @return the game type
     */
    public String getGameFuseType() {
        return "TicTacToe";
    }
}
