package org.alexdev.kepler.game.games.battleball;

import org.alexdev.kepler.game.games.GameTile;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileColour;
import org.alexdev.kepler.game.games.battleball.enums.BattleballTileState;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.games.player.GameTeam;
import org.alexdev.kepler.game.games.utils.FloodFill;
import org.alexdev.kepler.game.pathfinder.Position;

import java.util.List;

public class BattleballTile extends GameTile  {
    private BattleballTileColour colour;
    private BattleballTileState state;

    public BattleballTile(Position position) {
        super(position);
    }

    /**
     * Handle when a player jumps on a Battleball tile.
     *
     * @param gamePlayer the GamePlayer instance of the user jumping on the tile
     * @param updateTiles the tile list to add to if the tile requires an update
     * @param updateFillTiles the list to add to if these tiles require the filling in animation
     */
    public void interact(GamePlayer gamePlayer, List<BattleballTile> updateTiles, List<BattleballTile> updateFillTiles) {
        if (this.getColour() == BattleballTileColour.DISABLED) {
            return;
        }

        BattleballTileState state = this.getState();
        BattleballTileColour colour = this.getColour();

        GameTeam team = gamePlayer.getGame().getTeams().get(gamePlayer.getTeamId());

        if (colour == BattleballTileColour.DISABLED) {
            return;
        }

        if (state != BattleballTileState.SEALED) {
            if (colour.getColourId() == gamePlayer.getTeamId()) {
                this.setState(BattleballTileState.getStateById(state.getTileStateId() + 1));
            } else {
                this.setState(BattleballTileState.TOUCHED);
                this.setColour(BattleballTileColour.getColourById(gamePlayer.getTeamId()));
            }

            BattleballTileState newState = this.getState();
            BattleballTileColour newColour = this.getColour();

            int newPoints = -1;
            boolean tileLocked = false;

            if (state != newState && newState == BattleballTileState.TOUCHED) {
                newPoints = 2;

                if (colour != newColour) {
                    newPoints = 4;
                }
            }

            if (state != newState && newState == BattleballTileState.CLICKED) {
                newPoints = 6;

                if (colour != newColour) {
                    newPoints = 8;
                }
            }

            if (state != newState && newState == BattleballTileState.PRESSED) {
                newPoints = 10;

                if (colour != newColour) {
                    newPoints = 12;
                }
            }

            if (state != newState && newState == BattleballTileState.SEALED) {
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

            for (BattleballTile neighbour : FloodFill.neighbours(gamePlayer.getGame(), this.getPosition())) {
                if (neighbour == null || neighbour.getState() == BattleballTileState.SEALED || neighbour.getColour() == BattleballTileColour.DISABLED) {
                    continue;
                }

                var fillTiles = FloodFill.getFill(gamePlayer, neighbour);

                if (fillTiles.size() > 1) {
                    for (BattleballTile filledTile : FloodFill.getFill(gamePlayer, neighbour)) {
                        if (filledTile.getState() == BattleballTileState.SEALED) {
                            continue;
                        }

                        team.setSealedTileScore();

                        filledTile.setColour(this.getColour());
                        filledTile.setState(BattleballTileState.SEALED);

                        updateFillTiles.add(filledTile);
                    }
                }
            }

            updateTiles.add(this);
        }
    }


    /**
     * Get the current colour of this tile
     *
     * @return the colour
     */
    public BattleballTileColour getColour() {
        return colour;
    }

    /**
     * Set the current colour of this tile
     *
     * @param colour the current colour
     */
    public void setColour(BattleballTileColour colour) {
        this.colour = colour;
    }

    /**
     * Get the current state of this tile
     *
     * @return the state
     */
    public BattleballTileState getState() {
        return state;
    }

    /**
     * Set the current state of this tile
     *
     * @param state the current colour
     */
    public void setState(BattleballTileState state) {
        this.state = state;
    }
}
