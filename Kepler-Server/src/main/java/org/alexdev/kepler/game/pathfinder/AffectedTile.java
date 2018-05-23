package org.alexdev.kepler.game.pathfinder;

import java.util.ArrayList;
import java.util.List;

public class AffectedTile {

    /**
     * Gets the affected tiles.
     *
     * @param length the length
     * @param width the width
     * @param y the pos X
     * @param y the pos Y
     * @param rotation the rotation
     * @return the affected tiles
     */
    public static List<Position> getAffectedTiles(int length, int width, int x, int y, int rotation) {
        List<Position> points = new ArrayList<>();

        if (length != width) {
            if (rotation == 0 || rotation == 4) {
                int l = length;
                length = width;
                width = l;
            }
        }

        for (int newX = x; newX < x + width; newX++) {
            for (int newY = y; newY < y + length; newY++) {
                Position pos = new Position(newX, newY);
                points.add(pos);
            }
        }
        
        return points;
    }
}