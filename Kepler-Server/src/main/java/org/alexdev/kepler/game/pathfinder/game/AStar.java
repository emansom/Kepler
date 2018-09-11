package org.alexdev.kepler.game.pathfinder.game;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;

import java.security.PublicKey;
import java.util.*;

import static org.alexdev.kepler.game.pathfinder.Pathfinder.DIAGONAL_MOVE_POINTS;

public class AStar {
	private final int width;
	private final int height;

	private final Map<Position, AreaNode> nodes = new HashMap<Position, AreaNode>();

	@SuppressWarnings("rawtypes")
	private final Comparator<AreaNode> fComparator = new Comparator<AreaNode>() {
		public int compare(AreaNode a, AreaNode b) {
			return Integer.compare(a.getFValue(), b.getFValue()); //ascending to get the lowest
		}
	};

	public AStar(int width, int height) {
		this.width = width;
		this.height = height;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Position point = new Position(x, y);
				this.nodes.put(point, new AreaNode(point));
			}
		}
	}

	public AreaNode getNode(Position position) {
	    for (var kvp : this.nodes.entrySet()) {
            if (kvp.getKey().equals(position)) {
                return kvp.getValue();
            }
        }

        return null;
    }

	public ArrayList<Position> calculateAStarNoTerrain(Entity entity, Position p1, Position p2) {
		List<AreaNode> openList = new ArrayList<>();
		List<AreaNode> closedList = new ArrayList<>();

		AreaNode destNode = this.getNode(p2);

		AreaNode currentNode = this.getNode(p1);
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
				AreaNode adjNode = this.getNode(adjPoint);

				if (Pathfinder.isValidStep(entity.getRoomUser().getRoom(), entity, currentNode.point, adjPoint, false)) {
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

	private ArrayList<Position> calculatePath(AreaNode destinationNode) {
		ArrayList<Position> path = new ArrayList<Position>();
		AreaNode node = destinationNode;
		while (node.parent != null) {
			path.add(node.point);
			node = node.parent;
		}

        Collections.reverse(path);

		return path;
	}

	private boolean isInsideBounds(Position point) {
		return point.getX() >= 0 &&
				point.getX() < this.width &&
				point.getY() >= 0 &&
				point.getY() < this.height;
	}
}