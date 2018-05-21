package org.alexdev.kepler.messages.outgoing.rooms.user;

import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityState;
import org.alexdev.kepler.game.entity.EntityStatus;
import org.alexdev.kepler.messages.headers.Outgoing;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class USER_STATUSES extends MessageComposer {
    private List<EntityState> states;

    public USER_STATUSES(ConcurrentLinkedQueue<Entity> entities) {
        createEntityStates(new ArrayList<>(entities));
    }

    public USER_STATUSES(List<Entity> users) {
        createEntityStates(users);
    }

    public USER_STATUSES(Entity entity) {
        List<Entity> entities = new ArrayList<>();
        entities.add(entity);

        createEntityStates(entities);
    }

    private void createEntityStates(List<Entity> entities) {
        this.states = new ArrayList<>();

        for (Entity user : entities) {
            this.states.add(new EntityState(
                    user.getDetails().getId(),
                    user.getRoomUser().getInstanceId(),
                    user.getDetails(),
                    user.getRoomUser().getPosition().copy(),
                    user.getRoomUser().getStatuses()));
        }
    }

    @Override
    public void compose(NettyResponse response) {
        for (EntityState states : states) {
            response.writeDelimeter(states.getInstanceId(), ' ');
            response.writeDelimeter(states.getPosition().getX(), ',');
            response.writeDelimeter(states.getPosition().getY(), ',');
            response.writeDelimeter(Double.toString(StringUtil.format(states.getPosition().getZ())), ',');
            response.writeDelimeter(states.getPosition().getHeadRotation(), ',');
            response.writeDelimeter(states.getPosition().getBodyRotation(), '/');

            for (Map.Entry<EntityStatus, String> set : states.getStatuses().entrySet()) {
                response.write(set.getKey().getStatusCode());
                response.write(set.getValue());
                response.write("/");
            }

            response.write(Character.toString((char) 13));
        }
    }

    @Override
    public short getHeader() {
        return Outgoing.G_STAT;
    }
}
