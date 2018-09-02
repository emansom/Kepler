package org.alexdev.kepler.messages.incoming.rooms;

import org.alexdev.kepler.game.games.player.GamePlayer;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.messages.outgoing.games.FULLGAMESTATUS;
import org.alexdev.kepler.messages.outgoing.rooms.HEIGHTMAP;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class G_HMAP implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        player.send(new HEIGHTMAP(player.getRoomUser().getRoom().getModel()));

        GamePlayer gamePlayer = player.getRoomUser().getGamePlayer();

        if (gamePlayer != null) {
            player.send(new FULLGAMESTATUS(gamePlayer.getGame(), gamePlayer));
        }
    }
}
