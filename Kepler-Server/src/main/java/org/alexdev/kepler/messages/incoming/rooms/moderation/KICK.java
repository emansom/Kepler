package org.alexdev.kepler.messages.incoming.rooms.moderation;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class KICK implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        Player target = PlayerManager.getInstance().getPlayerByName(reader.contents());
        if(target != null && target.getRoomUser().getRoom().getId() == player.getRoomUser().getRoom().getId() &&
                (player.getRoomUser().getRoom().isOwner(player.getDetails().getId()) || player.hasFuse("fuse_kick"))) {
            target.getRoomUser().setBeingKicked(true);
            target.getRoomUser().kick(false);
        }
    }
}
