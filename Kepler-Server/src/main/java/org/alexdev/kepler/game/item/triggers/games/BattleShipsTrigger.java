package org.alexdev.kepler.game.item.triggers.games;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.games.GameBattleShip;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.room.RoomUser;

import java.util.ArrayList;
import java.util.List;

public class BattleShipsTrigger extends GameTrigger {
    public BattleShipsTrigger(int roomId) {
        for (var kvp : this.getChairGroups()) {
            this.gameInstances.add(new GameBattleShip(roomId, kvp));
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
     * Gets the list of seats as a group for when people play a game to gether.
     *
     * @return the map of chair groups
     */
    @Override
    public List<List<int[]>> getChairGroups() {
        return new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new int[]{15, 3});
                add(new int[]{13, 3});
            }});

            add(new ArrayList<>() {{
                add(new int[]{8, 3});
                add(new int[]{6, 3});
            }});

            add(new ArrayList<>() {{
                add(new int[]{2, 4});
                add(new int[]{2, 6});
            }});

            add(new ArrayList<>() {{
                add(new int[]{2, 10});
                add(new int[]{2, 12});
            }});

            add(new ArrayList<>() {{
                add(new int[]{2, 16});
                add(new int[]{2, 18});
            }});
        }};
    }
}
