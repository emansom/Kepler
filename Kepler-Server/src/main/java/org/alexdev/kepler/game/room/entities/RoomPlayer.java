package org.alexdev.kepler.game.room.entities;

import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.managers.RoomTimerManager;
import org.alexdev.kepler.game.room.managers.RoomTradeManager;
import org.alexdev.kepler.game.room.public_rooms.walkways.WalkwaysEntrance;
import org.alexdev.kepler.game.room.public_rooms.walkways.WalkwaysManager;

import java.util.ArrayList;
import java.util.List;

public class RoomPlayer extends RoomEntity {
    private Player player;
    private RoomTimerManager timerManager;

    private int authenticateId;
    private int authenticateTelporterId;

    private boolean isWalkingAllowed;
    private boolean isWalking;
    private boolean beingKicked;
    private boolean needsUpdate;
    private boolean isTyping;
    private boolean isDiving;

    private Player tradePartner;
    private List<Item> tradeItems;
    private boolean tradeAccept;

    private String currentGameId;

    public RoomPlayer(Player player) {
        super(player);
        this.player = player;
        this.authenticateTelporterId = -1;

        this.tradeItems = new ArrayList<>();
        this.timerManager = new RoomTimerManager(this);
    }

    @Override
    public void reset() {
        super.reset();
        this.isWalkingAllowed = true;
        this.isWalking = false;
        this.beingKicked = false;
        this.isTyping = false;
        this.isDiving = false;

        this.authenticateId = -1;
        this.timerManager.resetTimers();

        RoomTradeManager.close(this);
    }

    /**
     * Kick a user from the room.
     *
     * @param allowWalking whether the user can interrupt themselves walking towards the door
     */
    public void kick(boolean allowWalking) {
        Position doorLocation = this.getRoom().getModel().getDoorLocation();

        // If we're standing in the door, immediately leave room
        if (this.getPosition().equals(doorLocation)) {
            this.getRoom().getEntityManager().leaveRoom(this.player, true);
            return;
        }

        // Attempt to walk to the door
        this.walkTo(doorLocation.getX(), doorLocation.getY());
        this.isWalkingAllowed = allowWalking;
        this.beingKicked = !allowWalking;

        // If user isn't walking, leave immediately
        if (!this.isWalking) {
            this.getRoom().getEntityManager().leaveRoom(this.player, true);
        }
    }

    @Override
    public void stopWalking() {
        super.stopWalking();

        WalkwaysEntrance entrance = WalkwaysManager.getInstance().getWalkway(this.getRoom(), this.getPosition());

        if (entrance != null) {
            Room room = WalkwaysManager.getInstance().getWalkwayRoom(entrance.getModelTo());

            if (room != null) {
                room.getEntityManager().enterRoom(this.player, entrance.getDestination());
                return;
            }

        }

        boolean enteredDoor = false;
        Position doorPosition = this.getRoom().getModel().getDoorLocation();

        if (doorPosition.equals(this.getPosition())) {
            enteredDoor = true;
        }

        if (this.getRoom().isPublicRoom()) {
            if (WalkwaysManager.getInstance().getWalkway(this.getRoom(), doorPosition) != null) {
                enteredDoor = false;
            }
        }

        // Leave room if the tile is the door and we are in a flat or we're being kicked
        if (enteredDoor || this.beingKicked) {
            this.getRoom().getEntityManager().leaveRoom(this.player, true);
            return;
        }
    }

    public RoomTimerManager getTimerManager() {
        return timerManager;
    }

    public void setTimerManager(RoomTimerManager timerManager) {
        this.timerManager = timerManager;
    }

    public int getAuthenticateId() {
        return authenticateId;
    }

    public void setAuthenticateId(int authenticateId) {
        this.authenticateId = authenticateId;
    }

    public int getAuthenticateTelporterId() {
        return authenticateTelporterId;
    }

    public void setAuthenticateTelporterId(int authenticateTelporterId) {
        this.authenticateTelporterId = authenticateTelporterId;
    }

    public boolean isWalkingAllowed() {
        return isWalkingAllowed;
    }

    public void setWalkingAllowed(boolean walkingAllowed) {
        isWalkingAllowed = walkingAllowed;
    }

    @Override
    public boolean isWalking() {
        return isWalking;
    }

    @Override
    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public boolean isBeingKicked() {
        return beingKicked;
    }

    public void setBeingKicked(boolean beingKicked) {
        this.beingKicked = beingKicked;
    }

    @Override
    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    @Override
    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public boolean isDiving() {
        return isDiving;
    }

    public void setDiving(boolean diving) {
        isDiving = diving;
    }

    public Player getTradePartner() {
        return tradePartner;
    }

    public void setTradePartner(Player tradePartner) {
        this.tradePartner = tradePartner;
    }

    public List<Item> getTradeItems() {
        return tradeItems;
    }

    public boolean hasAcceptedTrade() {
        return tradeAccept;
    }

    public void setTradeAccept(boolean tradeAccept) {
        this.tradeAccept = tradeAccept;
    }

    public String getCurrentGameId() {
        return currentGameId;
    }

    public void setCurrentGameId(String currentGameId) {
        this.currentGameId = currentGameId;
    }
}