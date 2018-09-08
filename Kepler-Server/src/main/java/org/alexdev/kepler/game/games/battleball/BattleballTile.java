package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.games.GameEvent;
import org.alexdev.kepler.game.games.GameObject;
import org.alexdev.kepler.game.games.GameTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballColourType;
import org.alexdev.kepler.game.games.battleball.enums.BattleballPlayerState;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileType;
import org.alexdev.kepler.game.games.battleball.events.AcquirePowerUpEvent;
import org.alexdev.kepler.game.games.battleball.events.PlayerUpdateEvent;
import org.alexdev.kepler.game.games.battleball.events.PowerUpSpawnEvent;
import org.alexdev.kepler.game.games.battleball.objects.PinObject;
import org.alexdev.kepler.game.games.battleball.objects.PowerObject;
import org.alexdev.kepler.game.games.battleball.objects.PowerUpUpdateObject;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.games.utils.FloodFill;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

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
            if (this.brokeBall(gamePlayer)) {
                return;
            }

            if (this.bounceWithPower(gamePlayer, updateTiles, updateFillTiles)) {
                return;
            }

            this.changeState(gamePlayer, updateTiles, updateFillTiles);
            this.checkPowerUp(gamePlayer, objects, events, updateTiles, updateFillTiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean brokeBall(GamePlayer gamePlayer) {
        for (GameObject gameObject : gamePlayer.getGame().getObjects()) {
            if (!(gameObject instanceof PinObject)) {
                continue;
            }

            PinObject pinObject = (PinObject) gameObject;

            if (gamePlayer.getPlayer().getRoomUser().getPosition().equals(pinObject.getPosition())) {
                gamePlayer.getPlayer().getRoomUser().stopWalking();
                gamePlayer.getPlayer().getRoomUser().setNextPosition(null);
                gamePlayer.getPlayer().getRoomUser().setWalkingAllowed(false);

                gamePlayer.setPlayerState(BattleballPlayerState.BALL_BROKEN);
                gamePlayer.getGame().getEventsQueue().add(new PlayerUpdateEvent(gamePlayer));

                GameScheduler.getInstance().getSchedulerService().schedule(()-> {
                    gamePlayer.setPlayerState(BattleballPlayerState.NORMAL);
                    gamePlayer.getPlayer().getRoomUser().setWalkingAllowed(true);
                    gamePlayer.getGame().getEventsQueue().add(new PlayerUpdateEvent(gamePlayer));
                }, 5, TimeUnit.SECONDS);

                return true;
            }
        }

        return false;
    }

    private boolean bounceWithPower(GamePlayer gamePlayer, List<BattleballTile> updateTiles, List<BattleballTile> updateFillTiles) {
        BattleballColourType colour = this.getColour();
        BattleballTileType state = this.getState();

        if (colour == BattleballColourType.DISABLED) {
            return false;
        }
        
        GameTeam team = gamePlayer.getGame().getTeams().get(gamePlayer.getTeamId());

        if (gamePlayer.getPlayerState() == BattleballPlayerState.HIGH_JUMPS) {
            this.setColour(BattleballColourType.getColourById(gamePlayer.getTeamId()));

            if (colour.getColourId() != team.getId() && state == BattleballTileType.SEALED) {
                this.setState(BattleballTileType.TOUCHED); // Only set to touched when bounching on other teams locked tile
                gamePlayer.setScore(gamePlayer.getScore() + 4);
            } else {
                this.setState(BattleballTileType.SEALED);
                team.setSealedTileScore();
            }

            this.checkFill(gamePlayer, updateTiles, updateFillTiles);
            updateTiles.add(this);

            return true;
        }

        if (gamePlayer.getPlayerState() == BattleballPlayerState.CLEANING_TILES) {
            if (colour == BattleballColourType.DEFAULT) {
                return true;
            }

            int pointsToRemove = 0;

            if (state == BattleballTileType.TOUCHED) {
                pointsToRemove = 2;
            }

            if (state == BattleballTileType.CLICKED) {
                pointsToRemove = 6;
            }

            if (state == BattleballTileType.PRESSED) {
                pointsToRemove = 10;
            }

            if (state == BattleballTileType.SEALED) {
                pointsToRemove = 14;
            }

            GameTeam oppositeTeam = gamePlayer.getGame().getTeams().get(colour.getColourId());
            int eachTeamRemove = pointsToRemove / oppositeTeam.getActivePlayers().size();

            for (GamePlayer p : oppositeTeam.getActivePlayers()) {
                p.setScore(p.getScore() - eachTeamRemove);
            }

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
        game.getObjects().remove(powerUp.getObject());

        powerUp.getTimeToDespawn().set(15);
        powerUp.setPlayerHolding(gamePlayer);

        events.add(new AcquirePowerUpEvent(gamePlayer, powerUp));
        objects.add(new PowerUpUpdateObject(powerUp));
    }

    private void changeState(GamePlayer gamePlayer, List<BattleballTile> updateTiles, List<BattleballTile> updateFillTiles) {
        if (this.getColour() == BattleballColourType.DISABLED) {
            return;
        }

        BattleballTileType state = this.getState();
        BattleballColourType colour = this.getColour();

        int teamId = gamePlayer.getHarlequinTeamId() != -1 ? gamePlayer.getHarlequinTeamId() : gamePlayer.getTeamId();
        GameTeam team = gamePlayer.getGame().getTeams().get(teamId);

        if (colour == BattleballColourType.DISABLED) {
            return;
        }

        if (state != BattleballTileType.SEALED) {
            if (colour.getColourId() == team.getId()) {
                this.setState(BattleballTileType.getStateById(state.getTileStateId() + 1));
            } else {
                if (gamePlayer.getGame().getMapId() == 5) { // Barebones classic takes 4 hits
                    this.setState(BattleballTileType.TOUCHED);
                    this.setColour(BattleballColourType.getColourById(teamId));
                } else {
                    this.setState(BattleballTileType.CLICKED);
                    this.setColour(BattleballColourType.getColourById(teamId));
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

                    // Increase score for other team if harlequin is enabled
                    if (gamePlayer.getHarlequinTeamId() != -1) {
                        int pointsAcrossTeams = newPoints / team.getActivePlayers().size();

                        for (GamePlayer p : team.getActivePlayers()) {
                            p.setScore(p.getScore() + pointsAcrossTeams);
                        }
                    } else {
                        gamePlayer.setScore(gamePlayer.getScore() + newPoints);
                    }
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
        int teamId = gamePlayer.getHarlequinTeamId() != -1 ? gamePlayer.getHarlequinTeamId() : gamePlayer.getTeamId();
        GameTeam team = gamePlayer.getGame().getTeams().get(teamId);

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
