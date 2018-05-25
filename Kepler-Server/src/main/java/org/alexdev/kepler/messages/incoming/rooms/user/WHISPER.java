package org.alexdev.kepler.messages.incoming.rooms.user;

import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.StringUtil;

public class WHISPER implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoom();

        if (room == null) {
            return;
        }

        String contents = reader.readString();

        String username = contents.substring(0, contents.indexOf(" "));
        String message = StringUtil.filterInput(contents.substring(username.length() + 1), true);

        CHAT_MESSAGE chatMessage = new CHAT_MESSAGE(
                CHAT_MESSAGE.type.WHISPER,
                player.getRoomUser().getInstanceId(),
                message);

        player.send(chatMessage);

        Player whisperUser = PlayerManager.getInstance().getPlayerByName(username);

            if (whisperUser != null) {
                whisperUser.send(chatMessage);
            }
    }
}
