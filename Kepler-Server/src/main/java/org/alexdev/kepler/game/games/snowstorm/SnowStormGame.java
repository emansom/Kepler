package org.alexdev.kepler.game.games.snowstorm;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameTile;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.models.RoomModel;
import org.alexdev.kepler.game.room.models.RoomModelManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SnowStormGame extends Game {
    private int gameLengthChoice;

    public SnowStormGame(int id, int mapId, String name, int teamAmount, Player gameCreator, int gameLengthChoice) {
        super(id, mapId, GameType.SNOWSTORM, name, teamAmount, gameCreator);
        this.gameLengthChoice = gameLengthChoice;
    }

    @Override
    public void initialise() {
        super.initialise();

        int seconds = 0;

        if (this.gameLengthChoice == 1) {
            seconds = (int) TimeUnit.MINUTES.toSeconds(2);
        }

        if (this.gameLengthChoice == 2) {
            seconds = (int) TimeUnit.MINUTES.toSeconds(3);
        }

        if (this.gameLengthChoice == 3) {
            seconds = (int) TimeUnit.MINUTES.toSeconds(5);
        }

        this.getTotalSecondsLeft().set(seconds); // Override with game length choice


        this.getRoom().setRoomModel(new RoomModel("snowwar_arena_0", "snowwar_arena_0", 0, 0, 0, 0,
                "", "none"));
    }


    @Override
    public void gameTick() {

    }

    @Override
    public boolean canTimerContinue() {
        return true;
    }

    @Override
    public void assignSpawnPoints() {

    }

    @Override
    public GameTile[][] getTileMap() {
        return new GameTile[0][];
    }

    @Override
    public void buildMap() {

    }

    @Override
    public List<GameEvent> getPersistentEvents() {
        return new ArrayList<>();
    }

    public int getGameLengthChoice() {
        return gameLengthChoice;
    }
}
