package org.alexdev.kepler.game.games.battleball.powerups;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.battleball.objects.PlayerUpdateObject;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HarlequinHandle {
    public static void handle(BattleballGame game, GamePlayer gamePlayer, Room room) {
        List<GamePlayer> affectedPlayers = new ArrayList<>();

        for (GamePlayer p : gamePlayer.getGame().getPlayers()) {
            if (p.getColouringForOpponentId() != -1 || p.getTeamId() == gamePlayer.getTeamId()) {
                continue;
            }

            if (p.getPlayerState() != BattleballPlayerState.NORMAL) {
                continue; // Don't override people using power ups
            }

            p.setPlayerState(BattleballPlayerState.COLORING_FOR_OPPONENT);
            p.setHarlequinPlayer(gamePlayer);

            game.getObjectsQueue().add(new PlayerUpdateObject(p));
            affectedPlayers.add(p);
        }

        GameScheduler.getInstance().getSchedulerService().schedule(()-> {
            if (game.isGameFinished()) {
                return;
            }

            for (GamePlayer p : affectedPlayers) {
                p.setPlayerState(BattleballPlayerState.NORMAL);
                p.setHarlequinPlayer(null);

                game.getObjectsQueue().add(new PlayerUpdateObject(p));
            }

        }, 10, TimeUnit.SECONDS);
    }
}
