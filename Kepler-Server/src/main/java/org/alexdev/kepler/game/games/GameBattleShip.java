package org.alexdev.kepler.game.games;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;

import java.util.List;

public class GameBattleShip extends GamehallGame {
    public GameBattleShip(int roomId, List<int[]> kvp) {
        super(roomId, kvp);
    }

    @Override
    public void handleCommand(Player player, Room room, Item item, String command, String[] args) {
        GameTrigger trigger = (GameTrigger) item.getItemTrigger();

        if (args[0].equals("CLOSE")) {
            trigger.onEntityLeave(player, player.getRoomUser(), item);
            return;
        }
    }

    @Override
    public int getMaximumPeopleRequired() {
        return 2;
    }

    @Override
    public int getMinimumPeopleRequired() {
        return 2;
    }

    @Override
    public String getGameFuseType() {
        return "BattleShip";
    }
}