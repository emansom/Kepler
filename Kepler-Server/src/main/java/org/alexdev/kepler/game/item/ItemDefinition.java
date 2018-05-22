package org.alexdev.kepler.game.item;

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
