package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.games.battleball.enums.BattleballPowerType;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.concurrent.ThreadLocalRandom;

public class BattleballPowerUp {
    private final int id;
    private final BattleballPowerType powerType;
    private final BattleballTile tile;
    private final Position position;

    public BattleballPowerUp(BattleballGame game, int id, BattleballTile tile) {
        this.id = id;
        this.tile = tile;
        this.position = this.tile.getPosition().copy();

        var allowedPowerUps = game.getAllowedPowerUps();
        this.powerType = BattleballPowerType.getById(allowedPowerUps[ThreadLocalRandom.current().nextInt(0, allowedPowerUps.length)]);
    }

    public int getId() {
        return id;
    }

    public BattleballPowerType getPowerType() {
        return powerType;
    }

    public Position getPosition() {
        return this.position;
    }

    public BattleballTile getTile() {
        return tile;
    }
}
