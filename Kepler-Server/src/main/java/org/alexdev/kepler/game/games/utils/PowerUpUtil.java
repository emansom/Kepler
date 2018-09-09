package org.alexdev.kepler.game.games.utils;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.battleball.objects.PlayerUpdateObject;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.mapping.RoomTile;

import java.util.concurrent.TimeUnit;

public class PowerUpUtil {
    public static void stunPlayer(Game game, GamePlayer gamePlayer, BattleballPlayerState state) {
        gamePlayer.getPlayer().getRoomUser().setWalkingAllowed(false);
        gamePlayer.getPlayer().getRoomUser().stopWalking();

        gamePlayer.setPlayerState(state);
        game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));

        // Restore player 5 seconds later
        GameScheduler.getInstance().getSchedulerService().schedule(()-> {
            gamePlayer.getPlayer().getRoomUser().setWalkingAllowed(true);

            gamePlayer.setPlayerState(BattleballPlayerState.NORMAL);
            game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));
        }, 5, TimeUnit.SECONDS);
    }

}
