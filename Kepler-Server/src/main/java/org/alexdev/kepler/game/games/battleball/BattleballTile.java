package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.GameTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileType;
import org.alexdev.kepler.game.games.battleball.events.AcquirePowerUpEvent;
import org.alexdev.kepler.game.games.battleball.events.PowerUpSpawnEvent;
import org.alexdev.kepler.game.games.battleball.objects.PowerObject;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.games.utils.FloodFill;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BattleballTile extends GameTile  {
    private BattleballColourType colour;
    private BattleballTileType state;

    public BattleballTile(Position position) {
        super(position);
    }

    /**
     * Handle when a player jumps on a Battleball tile.
     *  @param gamePlayer the GamePlayer instance of the user jumping on the tile
     * @param objects
     * @param updateTiles the tile list to add to if the tile requires an update
     * @param updateFillTiles the list to add to if these tiles require the filling in animation
     */
    public void interact(GamePlayer gamePlayer, List<GameObject> objects, List<GameEvent> events, List<BattleballTile> updateTiles, List<BattleballTile> updateFillTiles) {
        try {
            if (this.bounceWithPower(gamePlayer, updateTiles, updateFillTiles)) {
                return;
            }

            this.changeState(gamePlayer, updateTiles, updateFillTiles);
            this.checkPowerUp(gamePlayer, objects, events, updateTiles, updateFillTiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean bounceWithPower(GamePlayer gamePlayer, List<BattleballTile> updateTiles, List<BattleballTile> updateFillTiles) {
        GameTeam team = gamePlayer.getGame().getTeams().get(gamePlayer.getTeamId());

        if (gamePlayer.getPlayerState() == BattleballPlayerState.HIGH_JUMPS) {
            this.setColour(BattleballColourType.getColourById(gamePlayer.getTeamId()));
            this.setState(BattleballTileType.SEALED);

            this.checkFill(gamePlayer, updateTiles, updateFillTiles);
            updateTiles.add(this);

            team.setSealedTileScore();
            return true;
        }

        if (gamePlayer.getPlayerState() == BattleballPlayerState.CLEANING_TILES) {
            this.setColour(BattleballColourType.DEFAULT);
            this.setState(BattleballTileType.DEFAULT);
            updateTiles.add(this);

            return true;
        }

        return false;
    }

    private void checkPowerUp(GamePlayer gamePlayer, List<GameObject> objects, List<GameEvent> events, List<BattleballTile> updateTiles, List<BattleballTile> updateFillTiles) {
        BattleballGame game = (BattleballGame) gamePlayer.getGame();
        BattleballPowerUp powerUp = null;

        for (BattleballPowerUp power : game.getActivePowers()) {
            if (power.getTile().getPosition().equals(this.getPosition())) {
                powerUp = power;
                break;
            }
        }

        if (powerUp == null) {
            return;
        }

        if (!game.getStoredPowers().containsKey(gamePlayer)) {
            game.getStoredPowers().put(gamePlayer, new CopyOnWriteArrayList<>());
        }

        game.getActivePowers().clear();
        game.getStoredPowers().get(gamePlayer).add(powerUp);

        powerUp.getTimeToDespawn().set(15);
        powerUp.setPlayerHolding(gamePlayer);

        events.add(new AcquirePowerUpEvent(gamePlayer, powerUp));
        objects.add(new PowerObject(gamePlayer, powerUp));
    }

    private void changeState(GamePlayer gamePlayer, List<BattleballTile> updateTiles, List<BattleballTile> updateFillTiles) {
        if (this.getColour() == BattleballColourType.DISABLED) {
            return;
        }

        BattleballTileType state = this.getState();
        BattleballColourType colour = this.getColour();

        GameTeam team = gamePlayer.getGame().getTeams().get(gamePlayer.getTeamId());

        if (colour == BattleballColourType.DISABLED) {
            return;
        }

        if (state != BattleballTileType.SEALED) {
            if (colour.getColourId() == gamePlayer.getTeamId()) {
                this.setState(BattleballTileType.getStateById(state.getTileStateId() + 1));
            } else {
                if (gamePlayer.getGame().getMapId() == 5) { // Barebones classic takes 4 hits
                    this.setState(BattleballTileType.TOUCHED);
                    this.setColour(BattleballColourType.getColourById(gamePlayer.getTeamId()));
                } else {
                    this.setState(BattleballTileType.CLICKED);
                    this.setColour(BattleballColourType.getColourById(gamePlayer.getTeamId()));
                }
            }

            BattleballTileType newState = this.getState();
            BattleballColourType newColour = this.getColour();

            int newPoints = -1;
            boolean tileLocked = false;

            if (state != newState && newState == BattleballTileType.TOUCHED) {
                newPoints = 2;

                if (colour != newColour) {
                    newPoints = 4;
                }
            }

            if (state != newState && newState == BattleballTileType.CLICKED) {
                newPoints = 6;

                if (colour != newColour) {
                    newPoints = 8;
                }
            }

            if (state != newState && newState == BattleballTileType.PRESSED) {
                newPoints = 10;

                if (colour != newColour) {
                    newPoints = 12;
                }
            }

            if (state != newState && newState == BattleballTileType.SEALED) {
                newPoints = 14;
                tileLocked = true;
            }

            if (newPoints != -1) {
                if (!tileLocked) {
                    gamePlayer.setScore(gamePlayer.getScore() + newPoints);
                } else {
                    // Tile got sealed, so increase every team members' points
                    team.setSealedTileScore();
                }
            }

            this.checkFill(gamePlayer, updateTiles, updateFillTiles);
            updateTiles.add(this);
        }
    }

    private void checkFill(GamePlayer gamePlayer, List<BattleballTile> updateTiles, List<BattleballTile> updateFillTiles) {
        GameTeam team = gamePlayer.getGame().getTeams().get(gamePlayer.getTeamId());

        for (BattleballTile neighbour : FloodFill.neighbours(gamePlayer.getGame(), this.getPosition())) {
            if (neighbour == null || neighbour.getState() == BattleballTileType.SEALED || neighbour.getColour() == BattleballColourType.DISABLED) {
                continue;
            }

            var fillTiles = FloodFill.getFill(gamePlayer, neighbour);

            if (fillTiles.size() > 1) {
                for (BattleballTile filledTile : FloodFill.getFill(gamePlayer, neighbour)) {
                    if (filledTile.getState() == BattleballTileType.SEALED) {
                        continue;
                    }

                    team.setSealedTileScore();

                    filledTile.setColour(this.getColour());
                    filledTile.setState(BattleballTileType.SEALED);

                    updateFillTiles.add(filledTile);
                }
            }
        }
    }


    /**
     * Get the current colour of this tile
     *
     * @return the colour
     */
    public BattleballColourType getColour() {
        return colour;
    }

    /**
     * Set the current colour of this tile
     *
     * @param colour the current colour
     */
    public void setColour(BattleballColourType colour) {
        this.colour = colour;
    }

    /**
     * Get the current state of this tile
     *
     * @return the state
     */
    public BattleballTileType getState() {
        return state;
    }

    /**
     * Set the current state of this tile
     *
     * @param state the current colour
     */
    public void setState(BattleballTileType state) {
        this.state = state;
    }
}
