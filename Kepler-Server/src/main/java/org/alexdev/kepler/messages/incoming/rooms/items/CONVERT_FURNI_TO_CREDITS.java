package org.alexdev.kepler.messages.incoming.rooms.items;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.user.CREDIT_BALANCE;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class CONVERT_FURNI_TO_CREDITS implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoomUser().getRoom();

        if (room == null) {
            return;
        }

        if (!room.isOwner(player.getDetails().getId())) {
            return;
        }

        int itemId = Integer.parseInt(reader.contents());

        Item item = room.getItemManager().getById(itemId);

        if (item == null || !item.getBehaviour().isRedeemable()) {
            return;
        }

        // Sprite is of format CF_50_goldbar. This retrieves the 50 part
        Integer amount = Integer.parseInt(item.getDefinition().getSprite().split("_")[1]);

        room.getMapping().removeItem(item);
        ItemDao.deleteItem(item.getId());

        int currentAmount = PlayerDao.increaseCredits(amount, player.getDetails().getId());
        player.getDetails().setCredits(currentAmount);

        player.send(new CREDIT_BALANCE(player.getDetails()));
    }
}