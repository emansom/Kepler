package org.alexdev.kepler.game.games.battleball.powerups;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.BattleballTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileType;
import org.alexdev.kepler.game.games.battleball.objects.PlayerUpdateObject;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.room.Room;

import java.util.concurrent.TimeUnit;

public class SpringHandle {
    public static void handle(BattleballGame game, GamePlayer gamePlayer, Room room) {
        gamePlayer.setPlayerState(BattleballPlayerState.HIGH_JUMPS);
        game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));

        GameScheduler.getInstance().getSchedulerService().schedule(()-> {
            if (game.isGameFinished()) {
                return;
            }

            if (gamePlayer.getPlayerState() != BattleballPlayerState.HIGH_JUMPS) {
                return;
            }

            gamePlayer.setPlayerState(BattleballPlayerState.NORMAL);
            game.getObjectsQueue().add(new PlayerUpdateObject(gamePlayer));
        }, 10, TimeUnit.SECONDS);
    }
}
