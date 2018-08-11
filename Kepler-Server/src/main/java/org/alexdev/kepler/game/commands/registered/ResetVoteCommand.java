package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.UPDATE_VOTES;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;

public class ResetVoteCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add("fuse_administrator_access");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        player.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, player.getRoomUser().getInstanceId(), "Your vote for this room has been removed"));

        Room room = player.getRoomUser().getRoom();

        RoomDao.removeVote(player.getEntityId(), room.getId());
        room.getData().setRating(RoomDao.getRating(room.getId()));

        for (Player p : room.getEntityManager().getPlayers()) {
            boolean voted =RoomDao.hasVoted(p.getDetails().getId(), room.getId());
            p.send(new UPDATE_VOTES(voted, room.getData().getRating()));
        }
    }

    @Override
    public String getDescription() {
        return "If you regret your vote, this removes it";
    }
}
