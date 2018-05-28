package org.alexdev.kepler.game.room;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.pathfinder.Rotation;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.enums.DrinkType;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.game.room.public_rooms.PoolHandler;
import org.alexdev.kepler.game.room.public_rooms.walkways.WalkwaysEntrance;
import org.alexdev.kepler.game.room.public_rooms.walkways.WalkwaysManager;
import org.alexdev.kepler.game.room.tasks.WaveHandler;
import org.alexdev.kepler.game.texts.TextsManager;
import org.alexdev.kepler.messages.outgoing.rooms.user.USER_STATUSES;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomUser {
    private Entity entity;
    private Position position;
    private Position goal;
    private Position nextPosition;
    private Room room;
    private Item currentItem;

    private int instanceId;
    private int authenticateId;

    private Map<String, RoomUserStatus> statuses;
    private LinkedList<Position> path;

    private boolean isWalkingAllowed;
    private boolean isWalking;
    private boolean beingKicked;
    private boolean needsUpdate;
    private boolean isTyping;
    private boolean isDiving;
    private int lookTimer;

    public RoomUser(Entity entity) {
        this.entity = entity;
        this.reset();
    }

    public void reset() {
        this.nextPosition = null;
        this.currentItem = null;
        this.goal = null;
        this.room = null;

        this.isWalkingAllowed = true;

        this.isWalking = false;
        this.beingKicked = false;
        this.isTyping = false;
        this.isDiving = true;

        this.instanceId = -1;
        this.authenticateId = -1;
        this.lookTimer = -1;

        this.statuses = new ConcurrentHashMap<>();
        this.path = new LinkedList<>();
    }

    /**
     * Walk to specified position.
     *
     * @param X the x
     * @param Y the y
     */
    public void walkTo(int X, int Y) {
        if (!this.isWalkingAllowed) {
            return;
        }

        if (this.room == null) {
            return;
        }

        if (this.nextPosition != null) {
            this.position.setX(this.nextPosition.getX());
            this.position.setY(this.nextPosition.getY());
            this.updateNewHeight(this.position);
            this.needsUpdate = true;
        }

        RoomTile tile = this.room.getMapping().getTile(X, Y);

        if (tile == null) {
            return;
        }

        this.goal = new Position(X, Y);
        //System.out.println("User requested " + this.goal + " from " + this.position + " with item " + (tile.getHighestItem() != null ? tile.getHighestItem().getDefinition().getSprite() : "NULL"));

        LinkedList<Position> path = Pathfinder.makePath(this.entity);

        if (path.size() > 0) {
            this.path = path;
            this.isWalking = true;
        }
    }

    /**
     * Called to make a user stop walking.
     */
    public void stopWalking() {
        this.path.clear();
        this.isWalking = false;
        this.needsUpdate = true;
        this.nextPosition = null;
        this.removeStatus(StatusType.MOVE);

        WalkwaysEntrance entrance = WalkwaysManager.getInstance().getWalkway(this);

        if (entrance != null) {
            Room room = WalkwaysManager.getInstance().getWalkwayRoom(entrance.getModelTo());

            if (room != null) {
                room.getEntityManager().enterRoom(this.entity, entrance.getDestination());
                return;
            }

        }

        if (this.beingKicked) {
            this.room.getEntityManager().leaveRoom(this.entity, true);
            return;
        }

        this.invokeItem();

        // Use walk to next tile if on pool queue
        PoolHandler.checkPoolQueue(this.entity);
    }

    /**
     * Triggers the current item that the player has walked on top of.
     */
    public void invokeItem() {
        this.invokeItem(false);
    }

    /**
     * Triggers the current item that the player has walked on top of.
     */
    public void invokeItem(boolean silent) {
        boolean needsUpdate = false;
        double height = this.getTile().getTileHeight();

        if (height != this.position.getZ()) {
            this.position.setZ(height);
            needsUpdate = true;
        }

        RoomTile tile = this.getTile();

        Item item = null;

        if (tile.getHighestItem() != null) {
            item = tile.getHighestItem();
        }

        if (item == null || (!item.getDefinition().getBehaviour().isCanSitOnTop() || !item.getDefinition().getBehaviour().isCanLayOnTop())) {
            if (this.containsStatus(StatusType.SIT) || this.containsStatus(StatusType.LAY)) {
                this.removeStatus(StatusType.SIT);
                this.removeStatus(StatusType.LAY);
                needsUpdate = true;
            }
        }

        if (item != null) {
            if (item.getDefinition().getBehaviour().isCanSitOnTop()) {
                this.removeStatus(StatusType.DANCE);
                this.position.setRotation(item.getPosition().getRotation());
                this.setStatus(StatusType.SIT, " " + StringUtil.format(item.getDefinition().getTopHeight()));
                needsUpdate = true;
            }

            if (item.getDefinition().getBehaviour().isCanLayOnTop()) {
                this.removeStatus(StatusType.DANCE);
                this.position.setRotation(item.getPosition().getRotation());
                this.setStatus(StatusType.LAY, " " + StringUtil.format(item.getDefinition().getTopHeight()));
                needsUpdate = true;
            }

            if (item.getDefinition().getSprite().equals("poolBooth") ||
                item.getDefinition().getSprite().equals("poolExit") ||
                item.getDefinition().getSprite().equals("poolEnter") ||
                item.getDefinition().getSprite().equals("poolLift")) {
                PoolHandler.interact(item, this.entity);
            }
        }

        this.updateNewHeight(this.position);

        this.currentItem = item;

        if (silent) {
            this.needsUpdate = false;
        } else {
            this.needsUpdate = needsUpdate;
        }
    }


    public void carryItem(int carryId, String carryName) {
        DrinkType[] drinks = new DrinkType[26];
        drinks[1] = DrinkType.DRINK;  // Tea
        drinks[2] = DrinkType.DRINK;  // Juice
        drinks[3] = DrinkType.EAT;    // Carrot
        drinks[4] = DrinkType.EAT;    // Ice-cream
        drinks[5] = DrinkType.DRINK;  // Milk
        drinks[6] = DrinkType.DRINK;  // Blackcurrant
        drinks[7] = DrinkType.DRINK;  // Water
        drinks[8] = DrinkType.DRINK;  // Regular
        drinks[9] = DrinkType.DRINK;  // Decaff
        drinks[10] = DrinkType.DRINK; // Latte
        drinks[11] = DrinkType.DRINK; // Mocha
        drinks[12] = DrinkType.DRINK; // Macchiato
        drinks[13] = DrinkType.DRINK; // Espresso
        drinks[14] = DrinkType.DRINK; // Filter
        drinks[15] = DrinkType.DRINK; // Iced
        drinks[16] = DrinkType.DRINK; // Cappuccino
        drinks[17] = DrinkType.DRINK; // Java
        drinks[18] = DrinkType.DRINK; // Tap
        drinks[19] = DrinkType.DRINK; // H*bbo Cola
        drinks[20] = DrinkType.ITEM;  // Camera
        drinks[21] = DrinkType.EAT;   // Hamburger
        drinks[22] = DrinkType.DRINK; // Lime H*bbo Soda
        drinks[23] = DrinkType.DRINK; // Beetroot H*bbo Soda
        drinks[24] = DrinkType.DRINK; // Bubble juice from 1999
        drinks[25] = DrinkType.DRINK; // Lovejuice

        // Public rooms send the localised handitem name instead of the drink ID
        if (carryName != null) {
            for (int i = 0; i <= 25; i++) {
                String externalDrinkName = TextsManager.getInstance().getValue("handitem" + i);

                if (externalDrinkName != null && externalDrinkName.equals(carryName)) {
                    carryId = i;
                }
            }
        }

        // Not a valid drink ID
        if (carryId == 0 || carryId > 25) {
            return;
        }

        StatusType carryStatus = null;
        StatusType useStatus = null;

        DrinkType type = drinks[carryId];

        if (type == DrinkType.DRINK) {
            carryStatus = StatusType.CARRY_DRINK;
            useStatus = StatusType.USE_DRINK;
        }

        if (type == DrinkType.EAT) {
            carryStatus = StatusType.CARRY_FOOD;
            useStatus = StatusType.USE_FOOD;
        }

        if (type == DrinkType.ITEM) {
            carryStatus = StatusType.CARRY_ITEM;
            useStatus = StatusType.USE_ITEM;
        }

        this.removeStatus(StatusType.CARRY_ITEM);
        this.removeStatus(StatusType.CARRY_FOOD);
        this.removeStatus(StatusType.CARRY_DRINK);
        this.removeStatus(StatusType.DANCE);

        this.setStatus(carryStatus, " " + carryId, 120, useStatus, 12, 1);
        this.needsUpdate = true;
    }

    /**
     * Animates the users mouth when speaking and detects any gestures.
     *
     * @param message the text to read for any gestures and to find animation length
     * @param isShout whether the chat was a shout or not
     */
    public void showChat(String message, boolean isShout) {
        int talkDuration = 1;

        if (message.length() > 1) {
            if (message.length() >= 10) {
                talkDuration = 5;
            } else {
                talkDuration = (int) (message.length() / 2);
            }
        }

        String gesture = null;
        boolean gestureFound = false;

        if (message.contains(":)")
                || message.contains(":-)")
                || message.contains(":p")
                || message.contains(":d")
                || message.contains(":D")
                || message.contains(";)")
                || message.contains(";-)") ) {
            gesture = "sml";
            gestureFound = true;
        }

        if (!gestureFound &&
                message.contains(":s")
                || message.contains(":(")
                || message.contains(":-(")
                || message.contains(":'(") ) {
            gesture = "sad";
            gestureFound = true;
        }

        if (!gestureFound &&
                message.contains(":o")
                || message.contains(":O") ) {
            gesture = "srp";
            gestureFound = true;
        }


        if (!gestureFound &&
                message.contains(":@")
                || message.contains(">:(") ) {
            gesture = "agr";
            gestureFound = true;
        }

        if (gestureFound) {
            this.setStatus(StatusType.GESTURE, " " + gesture, 5, StatusType.BLANK, -1, -1);
        }

        this.setStatus(StatusType.TALK, "", talkDuration, StatusType.BLANK, -1, -1);
        this.needsUpdate = true;

        List<Player> players;

        if (isShout) {
            players = this.room.getEntityManager().getEntitiesByClass(Player.class);
        } else {
            players = new ArrayList<>();

            for (Player player : this.room.getEntityManager().getEntitiesByClass(Player.class)) {
                if (player.getEntityId() == this.entity.getEntityId()) {
                    continue;
                }

                if (player.getRoomUser().getPosition().getDistanceSquared(this.entity.getRoomUser().getPosition()) <= 10) {
                    players.add(player);
                }
            }
        }

        for (Player player : players) {
            player.getRoomUser().look(this.position);
        }
    }

    /**
     * Look towards a certain point.
     *
     * @param towards the coordinate direction to look towards
     */
    private void look(Position towards) {
        if (this.isWalking) {
            return;
        }

        int diff = this.position.getRotation() - Rotation.calculateHumanDirection(this.position.getX(), this.position.getY(), towards.getX(), towards.getY());


        if ((this.position.getRotation() % 2) == 0) {
            if (diff > 0) {
                this.position.setHeadRotation(this.position.getRotation() - 1);
            } else if (diff < 0) {
                this.position.setHeadRotation(this.position.getRotation() + 1);
            } else {
                this.position.setHeadRotation(this.position.getRotation());
            }
        }

        this.lookTimer = DateUtil.getCurrentTimeSeconds() + 6;
        this.needsUpdate = true;
    }

    /**
     * Force room user to wave
     */
    public void wave() {
        this.setStatus(StatusType.WAVE, "");

        if (!this.entity.getRoomUser().isWalking()) {
            this.room.send(new USER_STATUSES(List.of(this.entity)));
        }

        GameScheduler.getInstance().getScheduler().schedule(new WaveHandler(this.entity), 2, TimeUnit.SECONDS);

    }

    /**
     * Update new height.
     */
    public void updateNewHeight(Position position) {
        double height = this.room.getMapping().getTile(position).getTileHeight();
        double oldHeight = this.position.getZ();

        if (height != oldHeight) {
            this.position.setZ(height);
            this.needsUpdate = true;
        }
    }

    /**
     * Get the current tile the user is on.
     *
     * @return the room tile instance
     */
    public RoomTile getTile() {
        if (this.room == null) {
            return null;
        }

        return this.room.getMapping().getTile(this.position);
    }

    /**
     * Contains status.
     *
     * @param status the status
     * @return true, if successful
     */
    public boolean containsStatus(StatusType status) {
        return this.statuses.containsKey(status.getStatusCode());
    }

    /**
     * Removes the status.
     *
     * @param status the status
     * @return if the user contained the status
     */
    public void removeStatus(StatusType status) {
        this.statuses.remove(status.getStatusCode());
    }

    /**
     * Sets the status.
     *
     * @param status the status
     * @param value the value
     */
    public void setStatus(StatusType status, String value) {
        if (this.containsStatus(status)) {
            this.removeStatus(status);
        }

        this.statuses.put(status.getStatusCode(), new RoomUserStatus(status, value));
    }

    public void setStatus(StatusType status, String value, int secLifetime, StatusType action, int secActionSwitch, int secSwitchLifetime) {
        if (this.containsStatus(status)) {
            this.removeStatus(status);
        }

        this.statuses.put(status.getStatusCode(), new RoomUserStatus(status, value, secLifetime, action, secActionSwitch, secSwitchLifetime));
    }

    public Entity getEntity() {
        return entity;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getGoal() {
        return goal;
    }

    public void setGoal(Position goal) {
        this.goal = goal;
    }

    public Position getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(Position nextPosition) {
        this.nextPosition = nextPosition;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public Map<String, RoomUserStatus> getStatuses() {
        return this.statuses;
    }

    public LinkedList<Position> getPath() {
        return path;
    }

    public void setPath(LinkedList<Position> path) {
        this.path = path;
    }

    public boolean isWalkingAllowed() {
        return isWalkingAllowed;
    }

    public void setWalkingAllowed(boolean walkingAllowed) {
        isWalkingAllowed = walkingAllowed;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public void setBeingKicked(boolean beingKicked) {
        this.beingKicked = beingKicked;
    }

    public boolean isBeingKicked() {
        return beingKicked;
    }

    public int getAuthenticateId() {
        return authenticateId;
    }

    public void setAuthenticateId(int authenticateId) {
        this.authenticateId = authenticateId;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public boolean isDiving() {
        return isDiving;
    }

    public void setDiving(boolean diving) {
        isDiving = diving;
    }

    public int getLookTimer() {
        return lookTimer;
    }

    public void setLookTimer(int lookTimer) {
        this.lookTimer = lookTimer;
    }
}
