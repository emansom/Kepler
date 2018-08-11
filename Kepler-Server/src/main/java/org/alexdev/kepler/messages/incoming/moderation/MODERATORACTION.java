package org.alexdev.kepler.messages.incoming.moderation;

import org.alexdev.kepler.dao.mysql.ModerationDao;
import org.alexdev.kepler.game.moderation.ModerationActionType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.texts.TextsManager;
import org.alexdev.kepler.messages.incoming.rooms.user.HOTEL_VIEW;
import org.alexdev.kepler.messages.outgoing.user.ALERT;
import org.alexdev.kepler.messages.outgoing.user.MODERATOR_ALERT;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.encoding.FuseMessage;
import org.alexdev.kepler.util.encoding.VL64Encoding;

import java.util.List;

public class MODERATORACTION implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        int commandCat = reader.readInt();
        int commandId = reader.readInt();

        String alertMessage = reader.readString();
        String notes = reader.readString();

        if (commandCat == 0) {
            // User Command
            if (commandId == 0 && player.hasFuse("fuse_room_alert")) {
                String alertUser = reader.readString();

                Player target = PlayerManager.getInstance().getPlayerByName(alertUser);

                if (target != null) {
                    target.send(new MODERATOR_ALERT(alertMessage));
                    ModerationDao.addLog(ModerationActionType.ALERT_USER, player.getEntityId(), target.getEntityId(), alertMessage, notes);
                } else {
                    player.send(new ALERT("Target user is not online."));
                }
            } else if (commandId == 1 && player.hasFuse("fuse_kick")) {
                // Kick
                String alertUser = reader.readString();
                Player target = PlayerManager.getInstance().getPlayerByName(alertUser);

                if (target != null) {
                    if (target.getEntityId() == player.getEntityId()) {
                        return; // Can't kick yourself!
                    }

                    if (target.hasFuse("fuse_kick")) {
                        player.send(new ALERT(TextsManager.getInstance().getValue("modtool_rankerror")));
                        return;
                    }

                    target.getRoomUser().kick(false);
                    target.send(new HOTEL_VIEW());
                    target.send(new MODERATOR_ALERT(alertMessage));

                    ModerationDao.addLog(ModerationActionType.KICK_USER, player.getEntityId(), target.getEntityId(), alertMessage, notes);
                } else {
                    player.send(new ALERT("Target user is not online."));
                }
            } else if (commandId == 2 && player.hasFuse("fuse_ban")) {
                //Ban
                // TODO: Banning
            }
        } else if (commandCat == 1) {
            // Room Command
            if (commandId == 0 && player.hasFuse("fuse_room_alert")) {
                List<Player> players = player.getRoomUser().getRoom().getEntityManager().getPlayers();

                for (Player target : players) {
                    target.send(new MODERATOR_ALERT(alertMessage));
                }

                ModerationDao.addLog(ModerationActionType.ROOM_ALERT, player.getEntityId(), -1, alertMessage, notes);
            } else if (commandId == 1 && player.hasFuse("fuse_room_kick")) {
                // Room Kick
                List<Player> players = player.getRoomUser().getRoom().getEntityManager().getPlayers();

                for (Player target : players) {
                    if (target.hasFuse("fuse_room_kick")) {
                        continue;
                    }

                    target.getRoomUser().kick(false);

                    target.send(new HOTEL_VIEW());
                    target.send(new MODERATOR_ALERT(alertMessage));

                    ModerationDao.addLog(ModerationActionType.ROOM_KICK, player.getEntityId(), -1, alertMessage, notes);
                }
            }
        }
    }
}
