package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.dao.mysql.RoomDao;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.moderation.Fuseright;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.UPDATE_VOTES;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE.ChatMessageType;

public class ResetVoteCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add(Fuseright.DEFAULT);
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

        player.send(new CHAT_MESSAGE(ChatMessageType.WHISPER, player.getRoomUser().getInstanceId(), "Your vote for this room has been removed"));

        Room room = player.getRoomUser().getRoom();

        RoomDao.removeVote(player.getDetails(), room.getData());
        room.getData().setRating(RoomDao.getRating(room.getData()));

        for (Player p : room.getEntityManager().getPlayers()) {
            boolean voted = RoomDao.hasVoted(p.getDetails(), room.getData());
            p.send(new UPDATE_VOTES(voted, room.getData().getRating()));
        }
    }

    @Override
    public String getDescription() {
        return "If you regret your vote, this removes it";
    }
}
