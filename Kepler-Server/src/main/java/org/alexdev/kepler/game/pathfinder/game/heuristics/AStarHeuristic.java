package org.alexdev.kepler.game.pathfinder.game.heuristics;

import org.alexdev.kepler.game.pathfinder.Position;

import java.awt.Point;

/**
 * The interface AStar Heuristics have to implement.
 */
public interface AStarHeuristic {

	/**
	 * 
	 * The heuristic tries to guess how far a given Node is from the goal Node.
	 * The lower the cost, the more likely a Node will be searched next.
	 * 
	 * @param map The map on which we try to find the path
	 * @param x The x coordinate of the tile being evaluated
	 * @param y The y coordinate of the tile being evaluated
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The cost associated with the given tile
	 */
	public float getEstimatedDistanceToGoal(Position start, Position goal);
}
