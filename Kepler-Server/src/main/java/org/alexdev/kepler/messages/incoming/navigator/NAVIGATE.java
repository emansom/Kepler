package org.alexdev.kepler.messages.incoming.navigator;

import org.alexdev.kepler.game.navigator.NavigatorCategory;
import org.alexdev.kepler.game.navigator.NavigatorManager;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.messages.outgoing.navigator.NAVIGATE_LIST;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

import java.util.ArrayList;
import java.util.List;

public class NAVIGATE implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) {
        boolean hideFull = reader.readInt() == 1;
        int categoryId = reader.readInt();

        NavigatorCategory category = NavigatorManager.getInstance().getCategoryById(categoryId);

        if (category.getMinimumRoleAccess() > player.getDetails().getRank()) {
            return;
        }

        List<NavigatorCategory> subCategories = NavigatorManager.getInstance().getCategoriesByParentId(category.getId());
        List<Room> rooms = new ArrayList<>();

        int categoryCurrentVisitors = category.getCurrentVisitors();
        int categoryMaxVisitors = category.getMaxVisitors();

        for (Room room : RoomManager.getInstance().getRooms()) {
            if (hideFull && room.getData().getVisitorsNow() >= room.getData().getVisitorsMax()) {
                continue;
            }

            if (room.getData().getCategoryId() != category.getId()) {
                continue;
            }

            rooms.add(room);
        }

        player.send(new NAVIGATE_LIST(category, rooms, hideFull, subCategories, categoryCurrentVisitors, categoryMaxVisitors, player.getDetails().getRank()));

    }
}
