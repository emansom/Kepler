package org.alexdev.kepler.game.pathfinder.game;

import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.pathfinder.game.bresenhamsLine.BresenhamsLine;
import org.alexdev.kepler.game.pathfinder.game.heuristics.AStarHeuristic;
import org.alexdev.kepler.game.pathfinder.game.heuristics.DiagonalHeuristic;

import java.util.ArrayList;

public class PathFinder {
	AreaMap map;
	
	public ArrayList<Position> getWaypoints(AreaMap map) {
		this.map = map;

		AStarHeuristic heuristic = new DiagonalHeuristic();
		AStar aStar = new AStar(map, heuristic);

		ArrayList<Position> shortestPath = aStar.calcShortestPath(map.getStartLocationX(), map.getStartLocationY(), map.getGoalLocationX(), map.getGoalLocationY());

		ArrayList<Position> waypoints = calculateWayPoints(shortestPath);
		
		return waypoints;
	}
	
	private ArrayList<Position> calculateWayPoints(ArrayList<Position> shortestPath) {
		ArrayList<Position> waypoints = new ArrayList<>();
		
		shortestPath.add(0,map.getStartNode().getPoint());
		shortestPath.add(map.getGoalNode().getPoint());

		Position p1 = shortestPath.get(0);
		int p1Number = 0;
		waypoints.add(p1);

		Position p2 = shortestPath.get(1);
		int p2Number = 1;
		
		while(!p2.equals(shortestPath.get(shortestPath.size()-1))) {
			if(lineClear(p1, p2)) {
				//make p2 the next point in the path
				p2Number++;
				p2 = shortestPath.get(p2Number);
			} else {
				p1Number = p2Number-1;
				p1 = shortestPath.get(p1Number);
				waypoints.add(p1);
				p2Number++;
				p2 = shortestPath.get(p2Number);
			}
		}
		waypoints.add(p2);
		
		return waypoints;
	}
	
	private boolean lineClear(Position a, Position b) {
		ArrayList<Position> pointsOnLine = BresenhamsLine.getPointsOnLine(a, b);
		for(Position p : pointsOnLine) {
			if(map.getNode(p.getX(), p.getY()).isObstacle) {
				return false;
			}
		}
		return true;
	}
}
