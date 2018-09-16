package org.alexdev.kepler.game.pathfinder.game;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.game.room.mapping.RoomTileState;
import org.alexdev.kepler.game.room.models.RoomModel;

import java.security.PublicKey;
import java.util.*;

import static org.alexdev.kepler.game.pathfinder.Pathfinder.DIAGONAL_MOVE_POINTS;

public class AStar {
    private AreaNode[][] nodes;

    private final RoomModel roomModel;
	private final Comparator<AreaNode> fComparator = Comparator.comparingInt(AreaNode::getFValue);

	public AStar(RoomModel roomModel) {
		this.roomModel = roomModel;
		this.nodes = new AreaNode[roomModel.getMapSizeX()][roomModel.getMapSizeY()];

        for (int y = 0; y < this.roomModel.getMapSizeY(); y++) {
            for (int x = 0; x < this.roomModel.getMapSizeX(); x++) {
                this.nodes[x][y] = new AreaNode(new Position(x, y));
            }
        }
	}

	private AreaNode getNode(Position position) {
        if (position.getX() < 0 || position.getY() < 0) {
            return null;
        }

        if (position.getX() >= this.roomModel.getMapSizeX() || position.getY() >= this.roomModel.getMapSizeY()) {
            return null;
        }

        return this.nodes[position.getX()][position.getY()];
    }

	public LinkedList<Position> calculateAStarNoTerrain(Entity entity, Position p1, Position p2) {
		List<AreaNode> openList = new LinkedList<>();
		Set<AreaNode> closedList = new HashSet<>();

		AreaNode destNode = this.getNode(p2);
		AreaNode currentNode = this.getNode(p1);

		currentNode.parent = null;
		currentNode.setGValue(0);
		openList.add(currentNode);

		while (!openList.isEmpty()) {
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

                if (adjNode == null) {
                    continue;
                }

				boolean isFinalMove = adjPoint.equals(destNode.point);

				if (isValidStep(entity.getRoomUser().getRoom(), entity, currentNode.point.copy(), adjPoint.copy(), isFinalMove)) {
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

		return this.calculatePath(destNode);
	}

	/**
	 * Method for the pathfinder to check if the tile next to the current tile is a valid step.
	 *
	 * @param entity the entity walking
	 * @param current the current tile
	 * @param tmp the temporary tile around the current tile to check
	 * @param isFinalMove if the move was final
	 * @return true, if a valid step
	 */
	public static boolean isValidStep(Room room, Entity entity, Position current, Position tmp, boolean isFinalMove) {
		if (!RoomTile.isValidTile(room, entity, new Position(current.getX(), current.getY()))) {
			return false;
		}

		if (isFinalMove) {
            if (!RoomTile.isValidTile(room, entity, new Position(tmp.getX(), tmp.getY()))) {
                return false;
            }
        }

		RoomTile fromTile = room.getMapping().getTile(current);
		RoomTile toTile = room.getMapping().getTile(tmp);

		double oldHeight = fromTile.getWalkingHeight();
		double newHeight = toTile.getWalkingHeight();

		Item fromItem = fromTile.getHighestItem();
		Item toItem = toTile.getHighestItem();

		if (oldHeight - 3 >= newHeight) {
			return fromItem != null && (fromItem.hasBehaviour(ItemBehaviour.TELEPORTER)
					|| (fromItem.getDefinition().getSprite().equals("poolEnter"))//&& !entity.getRoomUser().containsStatus(StatusType.SWIM)) // Allow height difference only if they're heading to exit and swimming
					|| (fromItem.getDefinition().getSprite().equals("poolExit")));// && entity.getRoomUser().containsStatus(StatusType.SWIM))); // Allow height difference only if they're heading to entry and  not swimming
		}

		if (oldHeight + 1.5 <= newHeight) {
			return toItem != null && (toItem.hasBehaviour(ItemBehaviour.TELEPORTER)
					|| (toItem.getDefinition().getSprite().equals("poolEnter"))//&& !entity.getRoomUser().containsStatus(StatusType.SWIM)) // Allow height difference only if they're heading to exit and swimming
					|| (toItem.getDefinition().getSprite().equals("poolExit")));// && entity.getRoomUser().containsStatus(StatusType.SWIM))); // Allow height difference only if they're heading to entry and  not swimming
		}

		// Only check these below if the user is in a pool room.
		if (entity != null && (entity.getRoomUser().getRoom().getModel().getName().startsWith("pool_") ||
				entity.getRoomUser().getRoom().getModel().getName().equals("md_a"))) {
			if (toItem != null) {
				// Check if they have swimmers before trying to enter pool
				if (toItem.getDefinition().getSprite().equals("poolEnter") ||
						toItem.getDefinition().getSprite().equals("poolLeave")) {
					return entity.getDetails().getPoolFigure().length() > 0;
				}

				// Don't allow to "enter" the pool if they're already swimming
				if (entity.getRoomUser().containsStatus(StatusType.SWIM) &&
						toItem.getDefinition().getSprite().equals("poolEnter")) {
					return false;
				}

				// Don't allow to "leave" the pool if they're not swimming
				if (!entity.getRoomUser().containsStatus(StatusType.SWIM) &&
						toItem.getDefinition().getSprite().equals("poolExit")) {
					return false;
				}

				// Don't allow users to cut people in queue, force them to garound
				if (toItem.getDefinition().getSprite().equals("queue_tile2")) {
					RoomTile tile = room.getMapping().getTile(entity.getRoomUser().getGoal());

					if (tile.getHighestItem() == null || !tile.getHighestItem().getDefinition().getSprite().equals("queue_tile2")) {
						return false;
					}
				}

				// Don't allow people to enter the booth if it's closed, or don't allow
				// if they attempt to use the pool lift without swimmers
				if (toItem.getDefinition().getSprite().equals("poolBooth") ||
						toItem.getDefinition().getSprite().equals("poolLift")) {

					if (toItem.getCurrentProgramValue().equals("close")) {
						return false;
					} else {
						return !toItem.getDefinition().getSprite().equals("poolLift") || entity.getDetails().getPoolFigure().length() > 0;
					}
				}
			}
		}

		// Can't walk diagonal between two non-walkable tiles.
		if (current.getX() != tmp.getX() &&
				current.getY() != tmp.getY()) {

			boolean firstValidTile = RoomTile.isValidDiagonalTile(room, entity, new Position(tmp.getX(), current.getY()));
			boolean secondValidTile = RoomTile.isValidDiagonalTile(room, entity, new Position(current.getX(), tmp.getY()));

			if (!firstValidTile && !secondValidTile) {
				return false;
			}
		}

		// Avoid walking into furniture unless it's their last location
		if (!current.equals(room.getModel().getDoorLocation())) {
			if (toItem != null) {
				if (isFinalMove) {
					return toItem.isWalkable(tmp);
				} else {
					return toItem.hasBehaviour(ItemBehaviour.CAN_STAND_ON_TOP) || toItem.isGateOpen();
				}
			}
		}

		return true;
	}

	private LinkedList<Position> calculatePath(AreaNode destinationNode) {
		LinkedList<Position> path = new LinkedList<Position>();

		if (destinationNode.parent == null) {
		    //path.add(destinationNode.point);
		    return path;
        }

		AreaNode node = destinationNode;
		while (node.parent != null) {
			path.add(node.point);
			node = node.parent;
		}

        Collections.reverse(path);

		return path;
	}
}