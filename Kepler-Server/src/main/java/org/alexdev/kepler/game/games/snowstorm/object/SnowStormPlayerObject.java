package org.alexdev.kepler.game.games.snowstorm.object;

import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.battleball.BattleballPowerUp;
import org.alexdev.kepler.game.games.enums.GameObjectType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.snowstorm.SnowStormGameObject;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.ArrayList;
import java.util.List;

public class SnowStormPlayerObject extends SnowStormGameObject {
    private final GamePlayer gamePlayer;

    public SnowStormPlayerObject(GamePlayer gamePlayer) {
        super(GameObjectType.SNOWSTORM_PLAYER_OBJECT);
        this.gamePlayer = gamePlayer;
        this.getGameObjectsSyncValues().add(GameObjectType.SNOWSTORM_PLAYER_OBJECT.getObjectId()); // type id
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getInstanceId()); // int id
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getPosition().getX()); // x
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getPosition().getY()); // y
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getPosition().getRotation()); // body direction
        this.getGameObjectsSyncValues().add(0); // hit points
        this.getGameObjectsSyncValues().add(5); // snowball count
        this.getGameObjectsSyncValues().add(0); // is bot 
        this.getGameObjectsSyncValues().add(0); // activity timer
        this.getGameObjectsSyncValues().add(1); // activity state
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getPosition().getX()); // next tile x
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getPosition().getY()); // next tile y
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getPosition().getX()); // move target x
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getPosition().getY()); // move target y
        this.getGameObjectsSyncValues().add(0); // score
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getDetails().getId()); // player id
        this.getGameObjectsSyncValues().add(gamePlayer.getTeamId()); // team id
        this.getGameObjectsSyncValues().add(gamePlayer.getPlayer().getRoomUser().getRoom().getId()); // room index

        // ["type: 5",
        // "int_id: 4",
        // "x: 4",
        // "y: 0",
        // "body_direction: 5",
        // "hit_points: 0",
        // "snowball_count: 0",
        // "is_bot: 0",
        // "activity_timer: 0",
        // "activity_state: 4",
        // "next_tile_x: 4",
        // "next_tile_y: 4",
        // "move_target_x: 4",
        // "move_target_y: 0",
        // "score: 1",
        // "player_id: 0",
        // "team_id: 1",
        // "room_index: 1"]
        //"
    }

    @Override
    public void serialiseObject(NettyResponse response) {
        for (int syncValue : this.getGameObjectsSyncValues()) {
            System.out.println("Sync value: " + syncValue);
            response.writeInt(syncValue);
        }

        System.out.println("End of sync values");

        response.writeString(gamePlayer.getPlayer().getDetails().getName());
        response.writeString(gamePlayer.getPlayer().getDetails().getMotto());
        response.writeString(gamePlayer.getPlayer().getDetails().getFigure());
        response.writeString(gamePlayer.getPlayer().getDetails().getSex());// Actually room user id/instance id
    }
}
