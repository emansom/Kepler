package org.alexdev.kepler.game.pathfinder.game.heuristics;

import org.alexdev.kepler.game.pathfinder.Position;

import java.awt.Point;


/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile.
 */
public class ClosestHeuristic implements AStarHeuristic {

	public float getEstimatedDistanceToGoal(Position start, Position goal) {
		float dx = goal.getX() - start.getX();
		float dy = goal.getY() - start.getY();
		
		float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
		
		//Optimization! Changed to distance^2 distance: (but looks more "ugly")
		
		//float result = (float) (dx*dx)+(dy*dy);
		
		
		return result;
	}

}
