package org.alexdev.kepler.game.room.models.triggers;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.games.Game;
import org.alexdev.kepler.game.games.GameManager;
import org.alexdev.kepler.game.games.GameType;
import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.triggers.GameLobbyTrigger;
import org.alexdev.kepler.game.triggers.GenericTrigger;
import org.alexdev.kepler.messages.outgoing.games.LOUNGEINFO;
import org.alexdev.kepler.messages.outgoing.games.GAMEPLAYERINFO;
import org.alexdev.kepler.util.StringUtil;
import org.alexdev.kepler.util.config.GameConfiguration;

import java.util.List;
import java.util.Map;

public class BattleballLobbyTrigger extends GameLobbyTrigger {
    @Override
    public void onRoomEntry(Entity entity, Room room, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        player.send(new LOUNGEINFO());

        // Show you everyones score level
        player.send(new GAMEPLAYERINFO(this.getGameType(), room.getEntityManager().getPlayers()));

        // Show everyone your score level
        room.send(new GAMEPLAYERINFO(this.getGameType(), List.of(player)));
    }

    @Override
    public void onRoomLeave(Entity entity, Room room, Object... customArgs) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

    }

    @Override
    public void createGame(Player gameCreator, Map<String, Object> gameParameters) {
        if (!GameConfiguration.getInstance().getBoolean("battleball.create.game.enabled")) {
            return;
        }

        int mapId = (int) gameParameters.get("fieldType");

        if (mapId < 1 || mapId > 5) {
            return;
        }

        int teams = (int) gameParameters.get("numTeams");

        if (teams < 1 || teams > 4) {
            return;
        }

        String name = (String) gameParameters.get("name");

        if (name.isEmpty()) {
            return;
        }

        Game game = new Game(GameManager.getInstance().createId(), mapId, this.getGameType(), name, teams, gameCreator.getDetails().getId());

        GamePlayer gamePlayer = new GamePlayer(gameCreator);
        gamePlayer.setGameId(game.getId());
        gamePlayer.setTeamId(0);

        gameCreator.getRoomUser().setGamePlayer(gamePlayer);
        game.movePlayer(gamePlayer, -1, 0);

        String powerUps = (String) gameParameters.get("allowedPowerups");

        for (String powerUp : powerUps.split(",")) {
            game.getPowerUps().add(Integer.parseInt(powerUp));
        }

        GameManager.getInstance().getGames().add(game);
    }

    @Override
    public GameType getGameType() {
        return GameType.BATTLEBALL;
    }
}
