package org.alexdev.kepler.game.games;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.games.ITEMMSG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameTicTacToe extends GamehallGame {
    private static char[] validSides = new char[] {
        'O', 'X'
    };

    private List<Player> playersInGame;
    private Map<Player, Character> playerSides;

    public GameTicTacToe(int roomId, List<int[]> chairs) {
        super(roomId, chairs);
    }

    @Override
    public void gameStart() {
        this.playersInGame = new ArrayList<>();
        this.playerSides = new HashMap<>();
    }

    @Override
    public void gameStop() {
        this.playersInGame.clear();
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
                this.sendToEveryone(new ITEMMSG(new String[]{this.getGameId(), "TYPERESERVED"}));
                return;
            }

            this.playerSides.put(player, side);
            this.playersInGame.add(player);

            player.send(new ITEMMSG(new String[]{this.getGameId(), "SELECTTYPE", String.valueOf(side)}));

            String[] playerNames = new String[this.playersInGame.size()];

            for (int i = 0; i < this.playersInGame.size(); i++) {
                playerNames[i] = this.playersInGame.get(i).getDetails().getName();
            }

            // If only 1 player has chosen their side, then said that one only, when the 2nd player choses, both panels
            // will be updated the current teammate playing.
            if (playerNames.length == 2) {
                this.sendToEveryone(new ITEMMSG(new String[]{this.getGameId(), "OPPONENTS", playerNames[0], playerNames[1]}));
            } else {
                this.sendToEveryone(new ITEMMSG(new String[]{this.getGameId(), "OPPONENTS", playerNames[0]}));
            }
        }

        // Only allow restart game if the game has ended, but users can still end game if they walk away/disconnect etc
        if (command.equals("RESTART")) {
            // TODO: Clear game field
            return;
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
        return 2;
    }

    @Override
    public String getGameFuseType() {
        return "TicTacToe";
    }
}
