package org.alexdev.kepler.game.games;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.types.MessageComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class GamehallGame {
    private String gameId;
    private int roomId;

    private List<int[]> chairs;
    private List<Player> players;

    public GamehallGame(int roomId, List<int[]> chairs) {
        this.roomId = roomId;
        this.chairs = chairs;
        this.players = new CopyOnWriteArrayList<>();
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
     * Get the opponents (not including the user supplied).
     *
     * @param player the player to exclude
     * @return
     */
    public List<Player> getOpponents(Player player) {
        return this.players.stream().filter(p -> p.getEntityId() != player.getEntityId()).collect(Collectors.toList());
    }

    /**
     * Send a packet to all opponents except the user supplied
     *
     * @param player the player to exclude
     * @param messageComposer the message to send
     */
    public void sendToOpponents(Player player, MessageComposer messageComposer) {
        for (Player p : this.getOpponents(player)) {
            p.send(messageComposer);
        }
    }

    /**
     * Send a packet to everyone playing
     *
     * @param messageComposer the packet to send
     */
    public void sendToEveryone(MessageComposer messageComposer) {
        for (Player p : this.players) {
            p.send(messageComposer);
        }
    }

    /**
     * Add a player, will automatically assign it to the first or second player.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        if (this.players.contains(player)) {
            return;
        }

        this.players.add(player);
    }

    /**
     * Adds all players it finds from the chairs into the list.
     */
    public void addPlayers() {
        for (RoomTile roomTile : this.getTiles()) {
            if (roomTile.getEntities().size() > 0) {
                this.players.add((Player) roomTile.getEntities().get(0));
            }
        }
    }

    /**
     * Get if the server has the correct amount of players required before the game starts.
     *
     * @return true, if successful
     */
    public boolean hasPlayersRequired() {
        int players = 0;

        for (RoomTile roomTile : this.getTiles()) {
            if (roomTile.getEntities().size() > 0) {
                players++;
            }
        }

        return players >= 2;
    }

    /**
     * Return the room tiles for this room.
     *
     * @return the list of room tiles
     */
    public List<RoomTile> getTiles() {
        List<RoomTile> tiles = new ArrayList<>();
        Room room = this.getRoom();

        if (room == null) {
            return tiles;
        }

        for (var coord : this.chairs) {
            RoomTile roomTile = room.getMapping().getTile(coord[0], coord[1]);

            if (roomTile == null) {
                continue;
            }

            tiles.add(roomTile);
        }

        return tiles;
    }

    /**
     * Set the first and second player to null for when
     * the game ends.
     */
    public void removePlayers() {
        this.players.clear();
    }


    /**
     * Get FUSE game type
     *
     * @return the game type
     */
    public abstract String getGameFuseType();
}
