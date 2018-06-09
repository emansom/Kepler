package org.alexdev.kepler.messages.incoming.songs;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.dao.mysql.SongMachineDao;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.songs.HAND_SOUNDSETS;
import org.alexdev.kepler.messages.outgoing.songs.SOUNDSETS;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class INSERT_SOUND_PACKAGE implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws SQLException {
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

        Map<Integer, Integer> tracks = SongMachineDao.getTracks(room.getItemManager().getSoundMachine().getId());

        int soundSetId = reader.readInt();
        int slotId = 1;//reader.readInt() - 1;

        while (tracks.containsKey(slotId)) {
            slotId++;
        }

        if (tracks.containsKey(slotId) || slotId >= 5 || slotId < 0) {
            return;
        }

        int trackId = -1;
        Item trackItem = null;

        for (Item item : player.getInventory().getItems()) {
            if (item.hasBehaviour(ItemBehaviour.SOUND_MACHINE_SAMPLE_SET)) {
                int songId = Integer.parseInt(item.getDefinition().getSprite().split("_")[2]);

                if (songId == soundSetId) {
                    trackItem = item;
                    trackId = songId;
                }
            }
        }

        if (trackId == -1) {
            return;
        }

        player.getInventory().getItems().remove(trackItem);
        player.getInventory().getView("last");

        ItemDao.deleteItem(trackItem.getId());

        List<Integer> handSoundsets = new ArrayList<>();

        for (Item item : player.getInventory().getItems()) {
            if (item.hasBehaviour(ItemBehaviour.SOUND_MACHINE_SAMPLE_SET)) {
                handSoundsets.add(Integer.parseInt(item.getDefinition().getSprite().split("_")[2]));
            }
        }

        SongMachineDao.addTrack(room.getItemManager().getSoundMachine().getId(), soundSetId, slotId);

        player.send(new SOUNDSETS( SongMachineDao.getTracks(room.getItemManager().getSoundMachine().getId())));
        player.send(new HAND_SOUNDSETS(handSoundsets));
    }
}
