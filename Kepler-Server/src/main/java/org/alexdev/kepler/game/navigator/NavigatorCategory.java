package org.alexdev.kepler.game.navigator;

import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;

public class NavigatorCategory {
    private int id;
    private int parentId;
    private String name;
    private boolean publicSpaces;
    private boolean allowTrading;
    private int minimumRoleAccess;
    private int minimumRoleSetFlat;
    private boolean isNode;

    public NavigatorCategory(int id, int parentId, String name, boolean publicSpaces, boolean allowTrading, int minimumRoleAccess, int minimumRoleSetFlat, boolean isNode) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.publicSpaces = publicSpaces;
        this.allowTrading = allowTrading;
        this.minimumRoleAccess = minimumRoleAccess;
        this.minimumRoleSetFlat = minimumRoleSetFlat;
        this.isNode = isNode;
    }

    public int getCurrentVisitors() {
        int currentVisitors = 0;

        for (Room room : RoomManager.getInstance().getRooms()) {
            if (room.getData().getCategoryId() == this.id) {
                currentVisitors += room.getData().getVisitorsNow();
            }
        }

        return currentVisitors;
    }

    public int getMaxVisitors() {
        int maxVisitors = 0;

        for (Room room : RoomManager.getInstance().getRooms()) {
            if (room.getData().getCategoryId() == this.id) {
                maxVisitors += room.getData().getVisitorsMax();
            }
        }

        return maxVisitors;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public boolean isPublicSpaces() {
        return publicSpaces;
    }

    public boolean hasAllowTrading() {
        return allowTrading;
    }

    public int getMinimumRoleAccess() {
        return minimumRoleAccess;
    }

    public int getMinimumRoleSetFlat() {
        return minimumRoleSetFlat;
    }

    public boolean isNode() {
        return isNode;
    }
}
