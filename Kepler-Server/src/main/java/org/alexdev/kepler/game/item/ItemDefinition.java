package org.alexdev.kepler.game.item;

public class ItemDefinition {
    private String sprite;
    private ItemBehaviour behaviour;
    private String behaviourData;
    private double topHeight;
    private int length;
    private int width;
    private String colour;

    public ItemDefinition() {
        this.behaviour = new ItemBehaviour();
        this.sprite = "";
        this.behaviourData = "";
        this.colour = "";
        this.topHeight = 0;
        this.length = 1;
        this.width = 1;
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
}
