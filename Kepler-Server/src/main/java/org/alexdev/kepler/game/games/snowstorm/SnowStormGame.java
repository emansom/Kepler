package org.alexdev.kepler.game.games.snowstorm;

import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.GameTile;
import org.alexdev.kepler.game.games.enums.GameType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.models.RoomModel;
import org.alexdev.kepler.game.room.models.RoomModelManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class SnowStormGame extends Game {
    private int gameLengthChoice;
    private TurnContainer turnContainer;
    private List<GameObject> gameObjects;

    public SnowStormGame(int id, int mapId, String name, int teamAmount, Player gameCreator, int gameLengthChoice) {
        super(id, mapId, GameType.SNOWSTORM, name, teamAmount, gameCreator);
        this.gameLengthChoice = gameLengthChoice;
        this.turnContainer = new TurnContainer();
        this.gameObjects = new CopyOnWriteArrayList<>();
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
                "xxxxxxxxxxxxxxxxxxx00000xxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxx0000000xxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx000000000xxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxx00000000000xxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx0000000000000xxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx000000000000000xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxx00000000000000000xxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxx0000000000000000000xxxxxxxxxxxxxxxxxxx|xxxxxxxxxxx000000000000000000000xxxxxxxxxxxxxxxxxx|xxxxxxxxxx00000000000000000000000xxxxxxxxxxxxxxxxx|xxxxxxxxx0000000000000000000000000xxxxxxxxxxxxxxxx|xxxxxxxx000000000000000000000000000xxxxxxxxxxxxxxx|xxxxxxx00000000000000000000000000000xxxxxxxxxxxxxx|xxxxxx0000000000000000000000000000000xxxxxxxxxxxxx|xxxxx000000000000000000000000000000000xxxxxxxxxxxx|xxxx00000000000000000000000000000000000xxxxxxxxxxx|xxx0000000000000000000000000000000000000xxxxxxxxxx|xx000000000000000000000000000000000000000xxxxxxxxx|x00000000000000000000000000000000000000000xxxxxxxx|0000000000000000000000000000000000000000000xxxxxxx|0000000000000000000xxxxxxxxxx0xxxxxx00000000xxxxxx|0000000000000000000xxxxxxxxxx0xxxxxxx00000000xxxxx|0000000000000000000xxxxxxxxxx0xxxxxxx000000000xxxx|x000000000000000000xxx000000000000xxx0000000000xxx|xx00000000000000000xxx000000000000xxx00000000000xx|xxx0000000000000000xxx000000000000xxx000000000000x|xxxx000000000000000xxx000000000000xxx0000000000000|xxxxx00000000000000000000000000000xxx0000000000000|xxxxxx0000000000000xxx000000000000xxxx000000000000|xxxxxxx000000000000xxx000000000000xxxxxxxxxx0xxxxx|xxxxxxxx00000000000xxx000000000000xxxxxxxxxx0xxxxx|xxxxxxxxx0000000000xxx0000000000000xxxxxxxxx0xxxxx|xxxxxxxxxx000000000xxxxxxxx0000000000000000000000x|xxxxxxxxxxx00000000xxxxxxxxx00000000000000000000xx|xxxxxxxxxxxx00000000xxxxxxxx0000000000000000000xxx|xxxxxxxxxxxxx000000000000xxx000000000000000000xxxx|xxxxxxxxxxxxxx00000000000xxx00000000000000000xxxxx|xxxxxxxxxxxxxxx0000000000xxx0000000000000000xxxxxx|xxxxxxxxxxxxxxxx000000000xxx000000000000000xxxxxxx|xxxxxxxxxxxxxxxxx00000000xxx00000000000000xxxxxxxx|xxxxxxxxxxxxxxxxxx0000000xxx0000000000000xxxxxxxxx|xxxxxxxxxxxxxxxxxxx000000xxx000000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxx00000xxx00000000000xxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx0000xxx0000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx000xxx000000000xxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxx00xxx00000000xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxx0xxx0000000xxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxx000000xxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxx00000xxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxx0000xxxxxxxxxxxxxxxxxx|", "none"));
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

    public TurnContainer getTurnContainer() {
        return turnContainer;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}
