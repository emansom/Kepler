package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.pathfinder.Position;

public class BattleballTile {
    private Position position;
    private BattleballTileColour colour;
    private BattleballTileState state;
    private boolean isSpawnOccupied;

    public BattleballTile(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public BattleballTileColour getColour() {
        return colour;
    }

    public void setColour(BattleballTileColour colour) {
        this.colour = colour;
    }

    public BattleballTileState getState() {
        return state;
    }

    public void setState(BattleballTileState state) {
        this.state = state;
    }

    public boolean isSpawnOccupied() {
        return isSpawnOccupied;
    }

    public void setSpawnOccupied(boolean spawnOccupied) {
        isSpawnOccupied = spawnOccupied;
    }
}
