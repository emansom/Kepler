package org.alexdev.kepler.game.messenger;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerDetails;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.DateUtil;

public class MessengerUser {
    private final int userId;
    private String username;
    private String figure;
    private char sex;
    private String consoleMotto;
    private long lastOnline;

    public MessengerUser(PlayerDetails details) {
        this.userId = details.getId();
        this.username = details.getName();
        this.figure = details.getFigure();
        this.sex = details.getSex();
        this.consoleMotto = details.getConsoleMotto();
        this.lastOnline = details.getLastOnline();
    }

    public MessengerUser(int userId, String username, String figure, String sex, String consoleMotto, long lastOnline) {
        this.userId = userId;
        this.username = username;
        this.figure = figure;
        this.sex = sex.toLowerCase().equals("f") ? 'F' : 'M';
        this.lastOnline = lastOnline;
        this.consoleMotto = consoleMotto;
    }

    public void serialise(NettyResponse response) {
        Player player = PlayerManager.getInstance().getPlayerById(this.userId);

        if (player != null) {
            this.figure = player.getDetails().getFigure();
            this.lastOnline = player.getDetails().getLastOnline();
            this.sex = player.getDetails().getSex();
            this.consoleMotto = player.getDetails().getConsoleMotto();
        }

        response.writeInt(this.userId);
        response.writeString(this.username);
        response.writeBool(Character.toLowerCase(this.sex) == 'm');
        response.writeString(this.consoleMotto);

        boolean isOnline = (player != null);

        response.writeBool(isOnline);

        if (isOnline) {
            if (player.getRoomUser().getRoom() != null) {
                Room room = player.getRoomUser().getRoom();

                if (room.getData().getOwnerId() > 0) {
                    response.writeString("Floor1a");
                } else {
                    response.writeString(room.getData().getName());
                }
            } else {
                response.writeString("On hotel view");
            }
        } else {
            response.writeString(DateUtil.getDateAsString(this.lastOnline));
        }

        response.writeString(DateUtil.getDateAsString(this.lastOnline));
        response.writeString(this.figure);
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getConsoleMotto() {
        return consoleMotto;
    }

    public void setConsoleMotto(String consoleMotto) {
        this.consoleMotto = consoleMotto;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    @Override
    public String toString() {
        return "[" + username + "," + consoleMotto + "," + figure + "," + sex + "," + lastOnline + "]";
    }
}
