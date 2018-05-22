package org.alexdev.kepler.game.item;

import org.alexdev.kepler.game.texts.TextsManager;

public class ItemDefinition {
    private int id;
    private String sprite;
    private ItemBehaviour behaviour;
    private String behaviourData;
    private double topHeight;
    private double stackHeight;
    private int length;
    private int width;
    private String colour;

    public ItemDefinition() {
        this.behaviour = new ItemBehaviour();
        this.sprite = "";
        this.behaviourData = "";
        this.colour = "";
        this.topHeight = 0.01;
        this.stackHeight = 0.01;
        this.length = 1;
        this.width = 1;
    }

    public ItemDefinition(int id, String sprite, String behaviourData, double topHeight, int length, int width, String colour) {
        this.id = id;
        this.sprite = sprite;
        this.behaviourData = behaviourData;
        this.behaviour = ItemBehaviour.parse(this.behaviourData);
        this.topHeight = topHeight;
        this.length = length;
        this.width = width;
        this.colour = colour;
    }

    /**
     * Get the item name by creating an external text key and reading external text entries.
     *
     * @param specialSpriteId the special sprite id
     * @return the name
     */
    public String getName(int specialSpriteId) {
        if (this.behaviour.isDecoration()) {
            return this.sprite;
        }

        String etxernalTextKey = this.getExternalTextKey(specialSpriteId);
        String name = etxernalTextKey + "_name";

        String value = TextsManager.getInstance().getValue(etxernalTextKey);

        if (value == null) {
            return "null";
        }

        return name;
    }

    /**
     * Get the item description by creating an external text key and reading external text entries.
     *
     * @param specialSpriteId the special sprite id
     * @return the description
     */
    public String getDescription(int specialSpriteId) {
        if (this.behaviour.isDecoration()) {
            return this.sprite;
        }

        String etxernalTextKey = this.getExternalTextKey(specialSpriteId);
        String name = etxernalTextKey + "_desc";

        String value = TextsManager.getInstance().getValue(etxernalTextKey);

        if (value == null) {
            return "null";
        }

        return name;
    }

    /**
     * Create the catalogue icon through using the special sprite id.
     *
     * @param specialSpriteId the special sprite id
     * @return the catalogue icon
     */
    public String getIcon(int specialSpriteId) {
        String icon = "";

        icon += this.sprite;

        if (specialSpriteId > 0) {
            icon += " " + specialSpriteId;
        }

        return icon;
    }

    /**
     * Get external text key by definition.
     *
     * @param specialSpriteId the special sprite id
     * @return the external text key
     */
    private String getExternalTextKey(int specialSpriteId) {
        String key = "";

        if (specialSpriteId == 0) {
            if (this.behaviour.isWallItem()) {
                key = "wallitem";
            } else {
                key = "furni";
            }

            key += "_";
        }

        key += this.sprite;

        if (specialSpriteId > 0) {
            key += ("_" + specialSpriteId);
        }

        return key;
    }

    public int getId() {
        return id;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public double getTopHeight() {
        return topHeight;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public String getColour() {
        return colour;
    }

    public void setTopHeight(double topHeight) {
        this.topHeight = topHeight;
    }

    public ItemBehaviour getBehaviour() {
        return behaviour;
    }
    public String getBehaviourData() {
        return behaviourData;
    }

    public double getStackHeight() {
        return stackHeight;
    }
}
