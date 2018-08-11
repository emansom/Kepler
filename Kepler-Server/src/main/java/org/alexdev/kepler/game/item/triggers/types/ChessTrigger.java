package org.alexdev.kepler.game.item.triggers.types;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.games.GameChess;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.room.RoomUser;

import java.util.ArrayList;
import java.util.List;

public class ChessTrigger extends GameTrigger {
    public ChessTrigger(int roomId) {
        for (var kvp : this.getChairGroups()) {
            this.gameInstances.add(new GameChess(roomId, kvp));
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
    public List<List<int[]>> getChairGroups() {
        return new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new int[] { 2, 7});
                add(new int[] { 2, 9});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 6, 14});
                add(new int[] { 4, 10});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 12, 14});
                add(new int[] { 12, 12});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 13, 7});
                add(new int[] { 13, 5});
            }});

            add(new ArrayList<>() {{
                add(new int[] { 7, 3});
                add(new int[] { 9, 3});
            }});
        }};
    }

    /**
     * Get FUSE game type
     *
     * @return the game type
     */
    public String getGameFuseType() {
        return "Chess";
    }
}
