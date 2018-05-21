package org.alexdev.kepler.game.item;

import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.StringUtil;

public class Item {
    private int id;
    private ItemDefinition definition;
    private Position position;
    private String wallPosition;
    private boolean hasExtraParameter;
    private String currentProgram;
    private String customData;

    public Item() {
        this.id = 0;
        this.definition = new ItemDefinition();
        this.position = new Position();
        this.customData = "";
        this.wallPosition = "";
    }

    public void serialise(NettyResponse response) {
        if (!this.definition.getBehaviour().isPublicSpaceObject()) {
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
        } else {
            if (this.definition.getBehaviour().isWallItem()) {
                response.writeDelimeter(this.id, (char)9);
                response.writeDelimeter(this.definition.getSprite(), (char)9);
                response.writeDelimeter(" ", (char)9);
                response.writeDelimeter(this.wallPosition, (char)9);
                response.write(this.customData);
            } else {
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
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemDefinition getDefinition() {
        return definition;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
}

