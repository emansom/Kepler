package org.alexdev.kepler.game.room;

import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.room.models.RoomModel;
import org.alexdev.kepler.game.room.models.RoomModelManager;

public class RoomData {
    private Room room;

    private int id;
    private int ownerId;
    private String ownerName;
    private int categoryId;
    private String name;
    private String description;
    private String model;
    private String ccts;
    private int wallpaper;
    private int floor;
    private boolean showName;
    private boolean superUsers;
    private int accessType;
    private String password;
    private int visitorsNow;
    private int visitorsMax;

    public RoomData(Room room) {
        this.room = room;
    }

    public void fill(int id, int ownerId, int category, String name, String description, String model, String ccts, int wallpaper, int floor, boolean showName, boolean superUsers, int accessType, String password, int visitorsNow, int visitorsMax) {
        this.id = id;
        this.ownerId = ownerId;
        if (this.ownerId > 0) {
            this.ownerName = PlayerDao.getName(this.ownerId);
        } else {
            this.ownerName = "";
        }
        this.categoryId = category;
        this.name = name;
        this.description = description;
        this.model = model;
        this.ccts = ccts;
        this.wallpaper = wallpaper;
        this.floor = floor;
        this.showName = showName;
        this.superUsers = superUsers;
        this.accessType = accessType;
        this.password = password;
        this.visitorsNow = visitorsNow;
        this.visitorsMax = visitorsMax;

        for (var item : this.room.getModel().getPublicItems()) {
            item.setRoomId(this.id);
            this.room.getItems().add(item);
        }
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public String getCcts() {
        return ccts;
    }

    public void setCcts(String ccts) {
        this.ccts = ccts;
    }

    public int getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(int wallpaper) {
        this.wallpaper = wallpaper;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean showName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public boolean allowSuperUsers() {
        return superUsers;
    }

    public void setSuperUsers(boolean superUsers) {
        this.superUsers = superUsers;
    }

    public String getAccessType() {
        if (this.accessType == 2) {
            return "password";
        }

        if (this.accessType == 1) {
            return "closed";
        }

        return "open";
    }

    public int getAccessTypeId() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVisitorsNow() {
        return visitorsNow;
    }

    public void setVisitorsNow(int visitorsNow) {
        this.visitorsNow = visitorsNow;
    }

    public int getVisitorsMax() {
        return visitorsMax;
    }

    public void setVisitorsMax(int visitorsMax) {
        this.visitorsMax = visitorsMax;
    }
}
