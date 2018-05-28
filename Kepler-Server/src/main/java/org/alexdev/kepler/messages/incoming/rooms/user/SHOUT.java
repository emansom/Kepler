package org.alexdev.kepler.messages.incoming.rooms.user;

import io.netty.util.internal.ThreadLocalRandom;
import org.alexdev.kepler.game.commands.CommandManager;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.kepler.messages.outgoing.rooms.user.TYPING_STATUS;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.StringUtil;

public class SHOUT implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) {
        Room room = player.getRoom();

        if (room == null) {
            return;
        }

        String message = StringUtil.filterInput(reader.readString(), true);

        player.getRoomUser().setTyping(false);
        room.send(new TYPING_STATUS(player.getRoomUser().getInstanceId(), player.getRoomUser().isTyping()));

        if (CommandManager.getInstance().hasCommand(player, message)) {
            CommandManager.getInstance().invokeCommand(player, message);
            return;
        }

        player.getRoomUser().showChat(message, false);

        room.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.SHOUT, player.getRoomUser().getInstanceId(), message));
    }
}
