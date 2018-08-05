package org.alexdev.kepler.game.room;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.item.roller.RollingData;
import org.alexdev.kepler.game.pathfinder.Rotation;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.enums.DrinkType;
import org.alexdev.kepler.game.room.managers.RoomTimerManager;
import org.alexdev.kepler.game.room.managers.RoomTradeManager;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.game.room.public_rooms.PoolHandler;
import org.alexdev.kepler.game.room.public_rooms.SunTerraceHandler;
import org.alexdev.kepler.game.room.public_rooms.walkways.WalkwaysEntrance;
import org.alexdev.kepler.game.room.public_rooms.walkways.WalkwaysManager;
import org.alexdev.kepler.game.room.tasks.WaveTask;
import org.alexdev.kepler.game.texts.TextsManager;
import org.alexdev.kepler.messages.outgoing.rooms.user.USER_STATUSES;
import org.alexdev.kepler.util.StringUtil;
import org.alexdev.kepler.util.config.GameConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RoomUser {
    private Entity entity;
    private Position position;
    private Position goal;
    private Position nextPosition;
    private Room room;
    private Item currentItem;
    private RollingData rollingData;
    private RoomTimerManager timerManager;

    private int instanceId;
    private int authenticateId;
    private int authenticateTelporterId;

    private Map<String, RoomUserStatus> statuses;
    private LinkedList<Position> path;

    private boolean isWalkingAllowed;
    private boolean isWalking;
    private boolean beingKicked;
    private boolean needsUpdate;
    private boolean isTyping;
    private boolean isDiving;

    private Player tradePartner;
    private List<Item> tradeItems;
    private boolean tradeAccept;

    public RoomUser(Entity entity) {
        this.entity = entity;
        this.tradeItems = new ArrayList<>();
        this.statuses = new ConcurrentHashMap<>();
        this.path = new LinkedList<>();
        this.authenticateTelporterId = -1;
        this.timerManager = new RoomTimerManager(this);
        this.reset();
    }

    public void reset() {
        this.statuses.clear();
        this.path.clear();

        this.nextPosition = null;
        this.currentItem = null;
        this.goal = null;
        this.room = null;
        this.rollingData = null;

        this.isWalkingAllowed = true;
        this.isWalking = false;
        this.beingKicked = false;
        this.isTyping = false;
        this.isDiving = false;

        this.instanceId = -1;
        this.authenticateId = -1;
        this.timerManager.resetTimers();

        if (this.entity.getType() == EntityType.PLAYER) {
            RoomTradeManager.close(this);
        }
    }

    /**
     * Kick a user from the room.
     */
    public void kick() {
        Position doorLocation = this.room.getModel().getDoorLocation();

        // If we're standing in the door, immediately leave room
        if (this.position.equals(doorLocation)) {
            room.getEntityManager().leaveRoom(this.entity, true);
            return;
        }

        // Attempt to walk to the door
        this.walkTo(doorLocation.getX(), doorLocation.getY());

        // If user isn't walking, leave immediately
        if (!this.isWalking) {
            this.room.getEntityManager().leaveRoom(this.entity, true);
        }
    }

    /**
     * Make a user walk to specific coordinates. The goal must be valid and reachable.
     *
     * @param X the X coordinates
     * @param Y the Y coordinate
     */
    public void walkTo(int X, int Y) {
        if (this.room == null) {
            return;
        }

        if (!this.isWalkingAllowed) {
            return;
        }

        if (SunTerraceHandler.isRedirected(this, X, Y)) {
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
            //System.out.println("User requested " + X + ", " + Y + " from " + this.position);
            return;
        }

        this.goal = new Position(X, Y);
        //System.out.println("User requested " + this.goal + " from " + this.position + " with item " + (tile.getHighestItem() != null ? tile.getHighestItem().getDefinition().getSprite() : "NULL"));

        if (!RoomTile.isValidTile(this.room, this.entity, this.goal)) {
            return;
        }

        LinkedList<Position> path = Pathfinder.makePath(this.entity);

        if (path.size() > 0) {
            this.path = path;
            this.isWalking = true;
            this.timerManager.resetRoomTimer();
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

        this.invokeItem(false);

        // Use walk to next tile if on pool queue
        PoolHandler.checkPoolQueue(this.entity);
    }
    /**
     * Triggers the current item that the player has walked on top of.
     */
    public void invokeItem(boolean isRolling) {
        boolean needsUpdate = false;
        double height = this.getTile().getWalkingHeight();

        if (height != this.position.getZ()) {
            this.position.setZ(height);
            needsUpdate = true;
        }

        RoomTile tile = this.getTile();
        Item item = null;

        if (tile.getHighestItem() != null) {
            item = tile.getHighestItem();
        }

        if (item == null || (!item.hasBehaviour(ItemBehaviour.CAN_SIT_ON_TOP) || !item.hasBehaviour(ItemBehaviour.CAN_LAY_ON_TOP))) {
            if (this.containsStatus(StatusType.SIT) || this.containsStatus(StatusType.LAY)) {
                this.removeStatus(StatusType.SIT);
                this.removeStatus(StatusType.LAY);
                needsUpdate = true;
            }
        }

        if (item != null) {
            int headRotation = this.position.getHeadRotation();

            if (item.hasBehaviour(ItemBehaviour.CAN_SIT_ON_TOP)) {
                this.removeStatus(StatusType.DANCE);
                this.position.setRotation(item.getPosition().getRotation());
                this.setStatus(StatusType.SIT, StringUtil.format(item.getDefinition().getTopHeight()));
                needsUpdate = true;
            }

            if (item.hasBehaviour(ItemBehaviour.CAN_LAY_ON_TOP)) {
                this.removeStatus(StatusType.CARRY_ITEM);
                this.removeStatus(StatusType.CARRY_FOOD);
                this.removeStatus(StatusType.CARRY_DRINK);
                this.removeStatus(StatusType.DANCE);

                this.position.setRotation(item.getPosition().getRotation());
                this.setStatus(StatusType.LAY, StringUtil.format(item.getDefinition().getTopHeight()));
                needsUpdate = true;
            }

            if (isRolling) {
                if (needsUpdate && this.timerManager.getLookTimer() > -1) {
                    this.position.setHeadRotation(headRotation);
                }
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
        this.needsUpdate = needsUpdate;
    }

    /**
     * Assign a hand item to an entity, either by carry ID or carry name.
     *
     * @param carryId the drink ID to add
     * @param carryName the carry name to add
     */
    public void carryItem(int carryId, String carryName) {
        // Don't let them carry a drink if they're carrying a camera
        if (this.containsStatus(StatusType.CARRY_ITEM)) {
            return;
        }

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
        if (carryId <= 0 || carryId > 25) {
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

        if (carryStatus != StatusType.CARRY_ITEM) {
            this.setStatus(carryStatus, carryId, GameConfiguration.getInstance().getInteger("carry.timer.seconds"), useStatus, 12, 1);
        } else {
            this.setStatus(carryStatus, carryId); // Use camera for infinite time, don't switch between using and holding item.
        }

        this.needsUpdate = true;
    }

    /**
     * Animates the users mouth when speaking and detects any gestures.
     *
     * @param message the text to read for any gestures and to find animation length
     * @param isShout whether the chat was a shout or not
     */
    public void showChat(String message, boolean isShout) {
        if (message.endsWith("o/")) {
            this.wave();

            if (message.equals("o/")) {
                return; // Don't move mouth if it's just a wave
            }
        }

        String[] words = message.split(" ");
        int talkDuration = 1;

        if (words.length <= 5)
            talkDuration = words.length / 2;
        else
            talkDuration = 5;

        String gesture = null;
        boolean gestureFound = false;

        if (message.contains(":)")
                || message.contains(":-)")
                || message.contains(":p")
                || message.contains(":d")
                || message.contains(":D")
                || message.contains(";)")
                || message.contains(";-)")) {
            gesture = "sml";
            gestureFound = true;
        }

        if (!gestureFound &&
                message.contains(":s")
                || message.contains(":(")
                || message.contains(":-(")
                || message.contains(":'(")) {
            gesture = "sad";
            gestureFound = true;
        }

        if (!gestureFound &&
                message.contains(":o")
                || message.contains(":O")) {
            gesture = "srp";
            gestureFound = true;
        }


        if (!gestureFound &&
                message.contains(":@")
                || message.contains(">:(")) {
            gesture = "agr";
            gestureFound = true;
        }

        if (gestureFound) {
            this.setStatus(StatusType.GESTURE, gesture, 5, null, -1, -1);
        }

        this.setStatus(StatusType.TALK, "", talkDuration, null, -1, -1);
        this.needsUpdate = true;

        List<Player> players;

        if (isShout) {
            players = new ArrayList<>(this.room.getEntityManager().getPlayers());

            if (this.entity.getType() == EntityType.PLAYER) {
                Player player = (Player)this.entity;
                players.remove(player);
            }
        } else {
            players = new ArrayList<>();

            for (Player player : this.room.getEntityManager().getPlayers()) {
                if (player.getEntityId() == this.entity.getEntityId()) {
                    continue;
                }

                if (player.getRoomUser().getPosition().getDistanceSquared(this.entity.getRoomUser().getPosition()) <= 10) {
                    players.add(player);
                }
            }
        }

        for (Player player : players) {
            if (player.getRoomUser().containsStatus(StatusType.SLEEP)) {
                continue;
            }
            
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

        this.position.setHeadRotation(Rotation.getHeadRotation(this.position.getRotation(), this.position, towards));
        this.timerManager.beginLookTimer();
        this.needsUpdate = true;
    }

    /**
     * Force room user to wave
     */
    public void wave() {
        if (this.containsStatus(StatusType.WAVE)) {
            return;
        }

        if (this.containsStatus(StatusType.DANCE)) {
            this.removeStatus(StatusType.DANCE);
        }

        this.setStatus(StatusType.WAVE, "");

        if (!this.entity.getRoomUser().isWalking()) {
            this.room.send(new USER_STATUSES(List.of(this.entity)));
        }

        GameScheduler.getInstance().getSchedulerService().schedule(new WaveTask(this.entity), 2, TimeUnit.SECONDS);
    }

    /**
     * Update new height.
     */
    public void updateNewHeight(Position position) {
        if (this.room == null) {
            return;
        }

        RoomTile tile = this.room.getMapping().getTile(position);

        if (tile == null) {
            return;
        }

        double height = tile.getWalkingHeight();
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
     * Warps a user to a position, with the optional ability trigger an instant update.
     *
     * @param position the new position
     * @param instantUpdate whether the warping should show an instant update on the client
     */
    public void warp(Position position, boolean instantUpdate) {
        RoomTile oldTile = this.getTile();

        if (oldTile != null) {
            oldTile.removeEntity(this.entity);
        }

        this.position = position.copy();
        this.updateNewHeight(position);

        RoomTile newTile = this.getTile();

        if (newTile != null) {
            newTile.addEntity(this.entity);
        }

        if (instantUpdate && this.room != null) {
            this.room.send(new USER_STATUSES(List.of(this.entity)));
        }
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
    public void setStatus(StatusType status, Object value) {
        if (this.containsStatus(status)) {
            this.removeStatus(status);
        }

        this.statuses.put(status.getStatusCode(), new RoomUserStatus(status, value.toString()));
    }

    /**
     * Set a status with a limited lifetime, and optional swap to action every x seconds which lasts for
     * x seconds. Use -1 and 'null' for action and lifetimes to make it last indefinitely.
     *
     * @param status the status to add
     * @param value the status value
     * @param secLifetime the seconds of lifetime this status has in total
     * @param action the action to switch to
     * @param secActionSwitch the seconds to count until it switches to this action
     * @param secSwitchLifetime the lifetime the action lasts for before switching back.
     */
    public void setStatus(StatusType status, Object value, int secLifetime, StatusType action, int secActionSwitch, int secSwitchLifetime) {
        if (this.containsStatus(status)) {
            this.removeStatus(status);
        }

        this.statuses.put(status.getStatusCode(), new RoomUserStatus(status, value.toString(), secLifetime, action, secActionSwitch, secSwitchLifetime));
    }

    /**
     * Get if the entity is sitting on the ground, or on furniture which isn't a chair.
     *
     * @return true, if successful
     */
    public boolean isSittingOnGround() {
        if (this.currentItem == null || !this.currentItem.hasBehaviour(ItemBehaviour.CAN_SIT_ON_TOP)) {
            return this.containsStatus(StatusType.SIT);
        }

        return false;
    }

    /**
     * Get if the entity is sitting on a chair.
     *
     * @return true, if successful.
     */
    public boolean isSittingOnChair() {
        if (this.currentItem != null) {
            return this.currentItem.hasBehaviour(ItemBehaviour.CAN_SIT_ON_TOP);
        }

        return false;
    }

    /**
     * Get the status by status type.
     *
     * @param statusType the status type
     * @return the room user status instance
     */
    public RoomUserStatus getStatus(StatusType statusType) {
        if (this.statuses.containsKey(statusType.getStatusCode())) {
            return this.statuses.get(statusType.getStatusCode());
        }

        return null;
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

    public RoomTimerManager getTimerManager() {
        return timerManager;
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

    public Player getTradePartner() {
        return tradePartner;
    }

    public void setTradePartner(Player tradePartner) {
        this.tradePartner = tradePartner;
    }

    public boolean hasAcceptedTrade() {
        return tradeAccept;
    }

    public void setTradeAccept(boolean tradeAccept) {
        this.tradeAccept = tradeAccept;
    }

    public List<Item> getTradeItems() {
        return tradeItems;
    }

    public int getAuthenticateTelporterId() {
        return authenticateTelporterId;
    }

    public void setAuthenticateTelporterId(int authenticateTelporterId) {
        this.authenticateTelporterId = authenticateTelporterId;
    }

    public RollingData getRollingData() {
        return rollingData;
    }

    public void setRollingData(RollingData rollingData) {
        this.rollingData = rollingData;
    }
}
