package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.games.battleball.enums.BattleballPowerType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleballPowerUp {
    private final int id;
    private final AtomicInteger timeToDespawn;
    private final BattleballPowerType powerType;
    private final BattleballTile tile;
    private final Position position;
    private GamePlayer playerHolding;

    public BattleballPowerUp(BattleballGame game, int id, BattleballTile tile) {
        this.id = id;
        this.tile = tile;
        this.playerHolding = null;
        this.position = this.tile.getPosition().copy();
        this.timeToDespawn = new AtomicInteger(ThreadLocalRandom.current().nextInt(10, 20));
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

    public AtomicInteger getTimeToDespawn() {
        return timeToDespawn;
    }

    public void setPlayerHolding(GamePlayer playerHolding) {
        this.playerHolding = playerHolding;
    }

    public Integer getPlayerHolding() {
        if (this.playerHolding != null) {
            return this.playerHolding.getPlayer().getRoomUser().getInstanceId();
        }

        return -1;
    }
}
