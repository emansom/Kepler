package org.alexdev.kepler.game.room;

import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.util.StringUtil;
import org.alexdev.kepler.util.config.GameConfiguration;
import org.alexdev.kepler.util.config.ServerConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class RoomData {
    //private static final int SECONDS_UNTIL_UPDATE = 60;
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
    private boolean navigatorHide;
    private List<Room> childRooms;
    private int rating;

    RoomData(Room room) {
        this.room = room;
    }

    public void fill(int id, int ownerId, String ownerName, int category, String name, String description, String model, String ccts, int wallpaper, int floor, boolean showName, boolean superUsers, int accessType, String password, int visitorsNow, int visitorsMax, int rating) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerName = StringUtil.filterInput(ownerName, true);;
        this.categoryId = category;
        this.name = StringUtil.filterInput(name, true);
        this.description = StringUtil.filterInput(description, true);
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
        this.childRooms = new ArrayList<>();
        this.rating = rating;
        this.applyModelSettings();
    }

    public void applyModelSettings() {
        if (ServerConfiguration.getBoolean("debug")) {
            this.navigatorHide = false;
            return;
        }

        if (this.model.equals("rooftop_2")
                || this.model.equals("old_skool1")
                || this.model.equals("malja_bar_b")
                || this.model.equals("bar_b")
                || this.model.equals("pool_b")
                || this.model.equals("hallway0")
                || this.model.equals("hallway1")
                || this.model.equals("hallway3")
                || this.model.equals("hallway4")
                || this.model.equals("hallway5")
                || this.model.equals("hallway6")
                || this.model.equals("hallway7")
                || this.model.equals("hallway8")
                || this.model.equals("hallway10")
                || this.model.equals("hallway11")
                || this.model.equals("hallA")
                || this.model.equals("hallB")
                || this.model.equals("hallC")
                || this.model.equals("hallD")) {
            this.navigatorHide = true;
        }

        if (this.model.equals("rooftop")) {
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("rooftop_2"));
        }

        if (this.model.equals("old_skool0")) {
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("old_skool1"));
        }

        if (this.model.equals("malja_bar_a")) {
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("malja_bar_b"));
        }

        if (this.model.equals("pool_a")) {
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("pool_b"));
        }

        if (this.model.equals("bar_a")) {
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("bar_b"));
        }

        if (this.model.equals("entryhall")) {
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallA"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallB"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallC"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallD"));

        }

        if (this.model.equals("hallway2")) {
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway0"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway1"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway3"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway4"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway5"));
        }

        if (this.model.equals("hallway9")) {
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway10"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway11"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway8"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway7"));
            this.childRooms.add(RoomManager.getInstance().getRoomByModel("hallway6"));
        }
    }

    public int getTotalVisitorsNow() {
        if (this.childRooms.size() > 0) {
            int totalVisitors = this.visitorsNow;

            for (Room room : this.childRooms) {
                totalVisitors += room.getData().getVisitorsNow();
            }

            return totalVisitors;
        }

        return this.visitorsNow;
    }

    public int getTotalVisitorsMax() {
        if (this.childRooms.size() > 0) {
            int totalMaxVisitors = this.visitorsMax;

            for (Room room : this.childRooms) {
                totalMaxVisitors += room.getData().getVisitorsMax();
            }

            return totalMaxVisitors;
        }

        return this.visitorsMax;
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

    public String getPublicName() {
        if (this.room.isPublicRoom()) {
            if (this.name.startsWith("Upper Hallways")) {
                return "Upper Hallways";
            }

            if (this.name.startsWith("Lower Hallways")) {
                return "Lower Hallways";
            }

            if (this.name.startsWith("Club Massiva")) {
                return "Club Massiva";
            }

            if (this.name.startsWith("The Chromide Club")) {
                return "The Chromide Club";
            }

            if (this.ccts.equals("hh_room_gamehall,hh_games")) {
                return "Cunning Fox Gamehall";
            }
        }

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

    public boolean isNavigatorHide() {
        return navigatorHide;
    }

    public int getRating(){
        return this.rating;
    }

    public void setRating(int amount){
        this.rating = amount;
    }

}
