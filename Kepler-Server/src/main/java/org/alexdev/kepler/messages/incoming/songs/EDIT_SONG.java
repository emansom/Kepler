package org.alexdev.kepler.messages.incoming.songs;

import org.alexdev.kepler.dao.mysql.SongMachineDao;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.songs.HAND_SOUNDSETS;
import org.alexdev.kepler.messages.outgoing.songs.SONG_INFO;
import org.alexdev.kepler.messages.outgoing.songs.SOUNDSETS;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class EDIT_SONG implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        Room room = player.getRoomUser().getRoom();

        if (!room.isOwner(player.getEntityId())) {
            return;
        }

        if (room.getItemManager().getSoundMachine() == null) {
            return;
        }

        int songId = reader.readInt();

        player.send(new SONG_INFO(SongMachineDao.getSong(songId)));
        player.send(new SOUNDSETS(SongMachineDao.getTracks(room.getItemManager().getSoundMachine().getId())));
        player.send(new HAND_SOUNDSETS(player.getInventory().getSoundsets()));
    }
}
