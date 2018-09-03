package org.alexdev.kepler.messages.incoming.games;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.battleball.BattleballGame;
import org.alexdev.kepler.game.games.battleball.BattleballPowerUp;
import org.alexdev.kepler.game.games.battleball.events.ActivatePowerUpEvent;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class GAMEEVENT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (!player.getRoomUser().isWalkingAllowed()) {
            return;
        }

        GamePlayer gamePlayer = player.getRoomUser().getGamePlayer();

        if (gamePlayer == null) {
            return;
        }

        Game game = gamePlayer.getGame();

        int eventType = reader.readInt(); // Instance ID? Useless?

        // Walk request
        if (eventType == 0) {
            int X = reader.readInt();
            int Y = reader.readInt();

            System.out.println("X: " + X);
            System.out.println("Y: " + Y);

            //player.getRoomUser().walkTo(X, Y);
        }

        // Jump request
        if (eventType == 2) {
            int X = reader.readInt();
            int Y = reader.readInt();

            System.out.println("X: " + X);
            System.out.println("Y: " + Y);

            player.getRoomUser().walkTo(X, Y);
        }

        // Use power up request
        if (eventType == 4) {
            int powerId = reader.readInt();

            if (game instanceof BattleballGame) {
                BattleballGame battleballGame = (BattleballGame) game;

                if (!battleballGame.getStoredPowers().containsKey(gamePlayer)) {
                    return;
                }

                var powerList = battleballGame.getStoredPowers().get(gamePlayer);

                BattleballPowerUp powerUp = null;

                for (BattleballPowerUp power : powerList) {
                    if (power.getId() == powerId) {
                        powerUp = power;
                        break;
                    }
                }

                if (powerUp != null) {
                    battleballGame.getEventsQueue().add(new ActivatePowerUpEvent(gamePlayer, powerUp));
                    powerList.remove(powerUp);
                }
            }
        }
    }
}
