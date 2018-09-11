package org.alexdev.kepler.game.pathfinder.game;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.*;

import static org.alexdev.kepler.game.pathfinder.Pathfinder.DIAGONAL_MOVE_POINTS;

public class AStar {
	private final int width;
	private final int height;

	private final Map<Position, AreaMap> nodes = new HashMap<Position, AreaMap>();

	@SuppressWarnings("rawtypes")
	private final Comparator<AreaMap> fComparator = new Comparator<AreaMap>() {
		public int compare(AreaMap a, AreaMap b) {
			return Integer.compare(a.getFValue(), b.getFValue()); //ascending to get the lowest
		}
	};

	public AStar(int width, int height) {
		this.width = width;
		this.height = height;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Position point = new Position(x, y);
				this.nodes.put(point, new AreaMap(point));
			}
		}
	}

	public ArrayList<Position> calculateAStarNoTerrain(Entity entity, Position p1, Position p2) {
		List<AreaMap> openList = new ArrayList<>();
		List<AreaMap> closedList = new ArrayList<>();

		AreaMap destNode = this.nodes.get(p2);

		AreaMap currentNode = this.nodes.get(p1);
		currentNode.parent = null;
		currentNode.setGValue(0);
		openList.add(currentNode);

		while(!openList.isEmpty()) {
			openList.sort(this.fComparator);
			currentNode = openList.get(0);

			if (currentNode.point.equals(destNode.point)) {
				return this.calculatePath(destNode);
			}

			openList.remove(currentNode);
			closedList.add(currentNode);

			for (Position point : DIAGONAL_MOVE_POINTS) {
				Position adjPoint = currentNode.point.copy().add(point);
				AreaMap adjNode = null;

				if (Pathfinder.isValidStep(entity.getRoomUser().getRoom(), entity, currentNode.point, adjPoint, false)) {
					for (var kvp : this.nodes.entrySet()) {
						if (kvp.getKey().equals(adjPoint)) {
							adjNode = kvp.getValue();
						}
					}

					if (adjNode == null) {
						continue;
					}

					if (!closedList.contains(adjNode)) {
						if (!openList.contains(adjNode)) {
							adjNode.parent = currentNode;
							adjNode.calculateGValue(currentNode);
							adjNode.calculateHValue(destNode);
							openList.add(adjNode);
						} else {
							if (adjNode.gValue < currentNode.gValue) {
								adjNode.calculateGValue(currentNode);
								currentNode = adjNode;
							}
						}
					}
				}
			}
		}

		return null;
	}

	private ArrayList<Position> calculatePath(AreaMap destinationNode) {
		ArrayList<Position> path = new ArrayList<Position>();
		AreaMap node = destinationNode;
		while (node.parent != null) {
			path.add(node.point);
			node = node.parent;
		}
		return path;
	}

	private boolean isInsideBounds(Position point) {
		return point.getX() >= 0 &&
				point.getX() < this.width &&
				point.getY() >= 0 &&
				point.getY() < this.height;
	}
}