package org.alexdev.kepler.game.games;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameTicTacToe extends GamehallGame {
    private static char[] validSides = new char[] {
        'O', 'X'
    };

    private Map<Player, Character> playerSides;

    public GameTicTacToe(int roomId, List<int[]> chairs) {
        super(roomId, chairs);
    }

    @Override
    public void gameStart() {
        this.playerSides = new HashMap<>();
    }

    @Override
    public void gameStop() {
        this.playerSides.clear();
    }

    @Override
    public void handleCommand(Player player, Room room, Item item, String command, String[] args) {
        GameTrigger trigger = (GameTrigger) item.getItemTrigger();

        if (command.equals("CLOSE")) {
            trigger.onEntityLeave(player, player.getRoomUser(), item);
            return;
        }

        if (command.equals("CHOOSETYPE")) {
            char side = args[0].charAt(0);

            if (side != validSides[0] && side != validSides[1]) {
                return;
            }

            if (getPlayerBySide(side) != null) {
                return;
            }

            this.playerSides.put(player, side);
        }
    }

    /**
     * Locate a player instance by the side they're playing.
     *
     * @param side the side used
     * @return the player instance, if successful
     */
    public Player getPlayerBySide(char side) {
        for (var kvp : this.playerSides.entrySet()) {
            if (kvp.getValue() == side) {
                return kvp.getKey();
            }
        }

        return null;
    }

    @Override
    public int getMaximumPeopleRequired() {
        return 2;
    }

    @Override
    public int getMinimumPeopleRequired() {
        return 1;
    }

    @Override
    public String getGameFuseType() {
        return "TicTacToe";
    }
}
