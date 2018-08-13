package org.alexdev.kepler.game.games;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.games.ITEMMSG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameChess extends GamehallGame {
    private static class GameToken {
        private char token;

        private GameToken(char token) {
            this.token = token;
        }

        private char getToken() {
            return token;
        }
    }

    private GameToken[] gameTokens;

    private List<Player> playersInGame;
    private HashMap<Player, GameToken> playerSides;

    public GameChess(int roomId, List<int[]> chairs) {
        super(roomId, chairs);
    }

    @Override
    public void gameStart() {
        this.playersInGame =  new ArrayList<>();
        this.playerSides = new HashMap<>();
        this.restartMap();
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
            char sideChosen = args[0].charAt(0);
            player.send(new ITEMMSG(new String[]{this.getGameId(), "SELECTTYPE " + String.valueOf(sideChosen)}));
        }
    }

    /**
     * Get the names of the people currently playing, always returns an array with
     * a length of two, if the name is blank there's no player.
     *
     * @return the player names
     */
    private String[] getPlayerNames() {
        String[] playerNames = new String[]{"", ""};

        for (int i = 0; i < this.playersInGame.size(); i++) {
            Player player = this.playersInGame.get(i);
            playerNames[i] = this.playerSides.get(player) + " " + player.getDetails().getName();
        }

        return playerNames;
    }

    /**
     * Reset the game map.
     */
    private void restartMap() {
        this.gameTokens = new GameToken[]{
                new GameToken('O'),
                new GameToken('X')
        };
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
        return "Chess";
    }
}
