package org.alexdev.kepler.game.item;

import org.alexdev.kepler.game.item.base.ItemDefinition;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.messages.outgoing.rooms.items.SHOWPROGRAM;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.StringUtil;

public class Item {
    private int id;
    private int ownerId;
    private ItemDefinition definition;
    private int definitionId;
    private Position position;
    private String wallPosition;
    private boolean hasExtraParameter;
    private String customData;
    private int roomId;

    private String currentProgram;
    private String currentProgramValue;

    public Item() {
        this.id = 0;
        this.definition = new ItemDefinition();
        this.position = new Position();
        this.customData = "";
        this.wallPosition = "";
        this.currentProgram = "";
        this.currentProgramValue = "";
    }

    /**
     * Broadcast item program to current room, used for the pool lift, booth, pool ladders, etc
     * for special effects like splashing, closing/open curtains, etc.
     *
     * @param value the new program value to show
     */
    public void showProgram(String value) {
        if (value == null) {
            value = "";
        }

        this.currentProgramValue = value;

        Room room = this.getRoom();

        if (room != null) {
            room.send(new SHOWPROGRAM(this.currentProgram, this.currentProgramValue));
        }
    }

    public void updateEntities(Position oldPosition) {

    }

    /**
     * Get the total height, which is the height of the item plus stack size.
     *
     * @return the total height
     */
    public double getTotalHeight() {
        return this.position.getZ() + this.definition.getStackHeight();
    }

    /**
     * Get whether or not the item is walkable.
     *
     * @return true, if successful.
     */
    public boolean isWalkable() {
        if (this.definition.getBehaviour().isCanSitOnTop()) {
            return true;
        }

        if (this.definition.getBehaviour().isCanLayOnTop()) {
            return true;
        }

        if (this.definition.getBehaviour().isCanStandOnTop()) {
            return true;
        }

        if (this.definition.getBehaviour().isDoor()) {
            return this.customData.equals("O");
        }

        return false;
    }

    /**
     * Serialise item function for item handling packets.
     *
     * @param response the response to serialise to
     */
    public void serialise(NettyResponse response) {
        if (this.definition.getBehaviour().isPublicSpaceObject()) {
            response.writeDelimeter(this.customData, ' ');
            response.writeString(this.definition.getSprite());
            response.writeDelimeter(this.position.getX(), ' ');
            response.writeDelimeter(this.position.getY(), ' ');
            response.writeDelimeter((int) this.position.getZ(), ' ');
            response.write(this.position.getRotation());

            if (this.hasExtraParameter) {
                response.write(" 2");
            }

            response.write(Character.toString((char) 13));
        } else {
            if (this.definition.getBehaviour().isWallItem()) {
                response.writeDelimeter(this.id, (char) 9);
                response.writeDelimeter(this.definition.getSprite(), (char) 9);
                response.writeDelimeter(" ", (char) 9);
                response.writeDelimeter(this.wallPosition, (char) 9);

                if (this.customData.length() > 0) {
                    response.write(this.customData);
                }

                response.write(Character.toString((char) 13));

            } else {
                response.writeString(this.id);
                response.writeString(this.definition.getSprite());
                response.writeInt(this.position.getX());
                response.writeInt(this.position.getY());
                response.writeInt(this.definition.getLength());
                response.writeInt(this.definition.getWidth());
                response.writeInt(this.position.getRotation());
                response.writeString(StringUtil.format(this.position.getZ()));
                response.writeString(this.definition.getColour());
                response.writeString("");
                response.writeInt(0);
                response.writeString(this.customData);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public ItemDefinition getDefinition() {
        return definition;
    }

    public void setDefinitionId(int definitionId) {
        this.definition = ItemManager.getInstance().getDefinition(definitionId);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getWallPosition() {
        return wallPosition;
    }

    public void setWallPosition(String wallPosition) {
        this.wallPosition = wallPosition;
    }

    public boolean hasExtraParameter() {
        return hasExtraParameter;
    }

    public void setHasExtraParameter(boolean hasExtraParameter) {
        this.hasExtraParameter = hasExtraParameter;
    }

    public String getCurrentProgram() {
        return currentProgram;
    }

    public void setCurrentProgram(String currentProgram) {
        this.currentProgram = currentProgram;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public String getCurrentProgramValue() {
        return currentProgramValue;
    }

    public void setCurrentProgramValue(String currentProgramValue) {
        this.currentProgramValue = currentProgramValue;
    }

    public Room getRoom() {
        return RoomManager.getInstance().getRoomById(this.roomId);
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

}

