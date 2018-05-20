package org.alexdev.kepler.messages.outgoing.navigator;

import org.alexdev.kepler.game.navigator.NavigatorCategory;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.headers.Outgoing;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class NAVIGATE_LIST extends MessageComposer {
    private NavigatorCategory parentCategory;
    private List<Room> rooms;
    private boolean hideFull;
    private List<NavigatorCategory> subCategories;
    private int categoryCurrentVisitors;
    private int categoryMaxVisitors;

    public NAVIGATE_LIST(NavigatorCategory parentCategory, List<Room> rooms, boolean hideFull, List<NavigatorCategory> subCategories, int categoryCurrentVisitors, int categoryMaxVisitors) {
        this.parentCategory = parentCategory;
        this.rooms = rooms;
        this.hideFull = hideFull;
        this.subCategories = subCategories;
        this.categoryCurrentVisitors = categoryCurrentVisitors;
        this.categoryMaxVisitors = categoryMaxVisitors;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeBool(this.hideFull);
        response.writeInt(this.parentCategory.getId());
        response.writeInt(this.parentCategory.isPublicSpaces() ? 0 : 2);
        response.writeString(this.parentCategory.getName());
        response.writeInt(this.categoryCurrentVisitors);
        response.writeInt(this.categoryMaxVisitors);
        response.writeInt(this.parentCategory.getParentId());

        for (Room room : this.rooms) {
            if (room.isPublicRoom()) {
                response.writeInt(room.getData().getId());
                response.writeInt(1);
                response.writeString(room.getData().getName());
                response.writeInt(room.getData().getVisitorsNow());
                response.writeInt(room.getData().getVisitorsMax());
                response.writeInt(room.getData().getCategoryId());
                response.writeString(room.getData().getDescription());
                response.writeInt(room.getData().getId());
                response.writeInt(0);
                response.writeString(room.getData().getCcts());
                response.writeInt(0);
                response.writeInt(1);
            }
        }

        for (NavigatorCategory subCategory : this.subCategories) {
            response.writeInt(subCategory.getId());
            response.writeInt(subCategory.isPublicSpaces() ? 0 : 2);
            response.writeString(subCategory.getName());
            response.writeInt(subCategory.getCurrentVisitors());
            response.writeInt(subCategory.getMaxVisitors());
            response.writeInt(this.parentCategory.getId());
        }

    }

    @Override
    public short getHeader() {
        return Outgoing.NAVIGATE_LIST;
    }
}
