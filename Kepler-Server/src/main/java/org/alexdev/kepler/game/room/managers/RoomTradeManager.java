package org.alexdev.kepler.game.room.managers;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.game.room.enums.StatusType;
import org.alexdev.kepler.messages.outgoing.trade.TRADE_END;
import org.alexdev.kepler.messages.outgoing.trade.TRADE_WINDOW;

import java.util.ArrayList;
import java.util.List;

public class RoomTradeManager {

    /**
     * Close trade window, called when user leaves room, or closes
     * the trade window. Will close the partners trade window too.
     *
     * @param roomUser the room user to close the trade window for
     */
    public static void close(RoomUser roomUser) {
        Player player = (Player) roomUser.getEntity();

        if (roomUser.getTradePartner() != null) {
            player.send(new TRADE_END());
            player.getInventory().getView("last");

            roomUser.getTradePartner().send(new TRADE_END());
            roomUser.getTradePartner().getInventory().getView("last");

            reset(roomUser.getTradePartner().getRoomUser());
        }

        reset(roomUser);
    }

    /**
     * Resets all trade variables.
     *
     * @param roomUser the room user to reset the trade variables for
     */
    private static void reset(RoomUser roomUser) {
        roomUser.getTradeItems().clear();
        roomUser.setTradeAccept(false);
        roomUser.setTradePartner(null);

        roomUser.removeStatus(StatusType.TRADE);
        roomUser.setNeedsUpdate(true);
    }

    /**
     * Refresh the trade window, called when a user agrees/unagrees or adds
     * an item to the trade window. Will be ignored if they have no trade
     * partner.
     *
     * @param player the player to refresh the trade window for
     */
    public static void refreshWindow(Player player) {
        if (player.getRoomUser().getTradePartner() == null) {
            return;
        }

        Player tradePartner = player.getRoomUser().getTradePartner();

        player.send(new TRADE_WINDOW(
                player,
                player.getRoomUser().getTradeItems(),
                player.getRoomUser().hasAcceptedTrade(),
                tradePartner,
                tradePartner.getRoomUser().getTradeItems(),
                tradePartner.getRoomUser().hasAcceptedTrade()
        ));
    }

    /**
     * Adds an item from the trade partners offered items into the first parameter
     * players' inventory.
     *
     * @param player the player to add the items into
     * @param tradePartner the player to get the items offered from
     */
    public static void addItems(Player player, Player tradePartner) {
        List<Item> itemsToUpdate = new ArrayList<>();

        for (Item item : tradePartner.getRoomUser().getTradeItems()) {
            tradePartner.getInventory().getItems().remove(item);
            player.getInventory().getItems().add(item);

            item.setOwnerId(player.getEntityId());
            itemsToUpdate.add(item);
        }

        ItemDao.updateItems(itemsToUpdate);
    }
}
