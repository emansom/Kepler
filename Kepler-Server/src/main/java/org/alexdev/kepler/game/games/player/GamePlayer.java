package org.alexdev.kepler.game.games.player;

import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;

public class GamePlayer {
    private Player player;
    private int userId;
    private int instanceId;
    private int teamId;
    private Position position;

    public GamePlayer(Player player) {
        this.player = player;
        this.userId = player.getDetails().getId();
        this.teamId = -1;
        this.position = new Position();
    }

    public Player getPlayer() {
        return player;
    }

    public int getUserId() {
        return userId;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public Position getPosition() {
        return position;
    }
}
