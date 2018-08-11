package org.alexdev.kepler.game.item.games;

import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.mapping.RoomTile;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class GamehallGame {
    private String gameId;
    private int roomId;

    private Position chairPosition;
    private Position opponentPosition;

    private Player firstPlayer;
    private Player secondPlayer;

    public GamehallGame(int roomId, int[] chairPosition, int[] opponentPosition) {
        this.roomId = roomId;
        this.chairPosition = new Position(chairPosition[0], chairPosition[1]);
        this.opponentPosition = new Position(opponentPosition[0], opponentPosition[1]);
    }

    /**
     * Gets the unique game ID instance for this pair. Will
     * return null if game has not initialised.
     *
     * @return the game id
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Generate the unique game ID instance for this pair.
     */
    public void createGameId() {
        String alphabet = "abcdefghijlmnopqrstuvwyz";
        StringBuilder gameId = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            gameId.append(alphabet.charAt(ThreadLocalRandom.current().nextInt(0, alphabet.length())));
        }

        this.gameId = gameId.toString();
    }

    /**
     * Resets the game ID back to null.
     */
    public void resetGameId() {
        this.gameId = null;
    }

    /**
     * Get the room instance this game instance is running in.
     *
     * @return the room instance
     */
    public Room getRoom() {
        return RoomManager.getInstance().getRoomById(this.roomId);
    }

    /**
     * Get the chair tile for this position.
     *
     * @return the room tile, if successful
     */
    public RoomTile getChairTile() {
        return this.getTile(this.chairPosition);
    }

    /**
     * Get the opponent tile for this position.
     *
     * @return the opponent tile, if successful
     */
    public RoomTile getOpponentTile(Position position) {
        return this.chairPosition.equals(position) ? this.getTile(this.opponentPosition) : this.getTile(this.chairPosition);
    }

    /**
     * Get the room tile for the specified position
     *
     * @param position the position
     * @return the room tile, if successful
     */
    private RoomTile getTile(Position position) {
        Room room = this.getRoom();

        if (room != null) {
            return room.getMapping().getTile(position);
        }

        return null;
    }

    /**
     * Get the chair opposition that this instance is running in.
     *
     * @return the chair position.
     */
    public Position getChairPosition() {
        return chairPosition;
    }

    /**
     * Get the opponent position that this instance is running in.
     *
     * @return the opponent position.
     */
    public Position getOpponentPosition() {
        return opponentPosition;
    }

    /**
     * Get the opponent, aka the player which isn't the player in the parameters.
     *
     * @param player the player to not return
     * @return the opponent they're playing
     */
    public Player getOpponent(Player player) {
        if (this.firstPlayer == player) {
            return this.secondPlayer;
        }

        return firstPlayer;
    }
    /**
     * Add a player, will automatically assign it to the first or second player.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        if (this.firstPlayer == null) {
            this.firstPlayer = player;
            return;
        }

        if (this.secondPlayer == null) {
            this.secondPlayer = player;
            return;
        }
    }

    /**
     * Set the first and second player to null for when
     * the game ends.
     */
    public void removePlayers() {
        this.firstPlayer = null;
        this.secondPlayer = null;
    }
}
