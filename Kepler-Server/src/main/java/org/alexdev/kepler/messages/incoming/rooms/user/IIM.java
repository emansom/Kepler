package org.alexdev.kepler.messages.incoming.rooms.user;

import org.alexdev.kepler.game.games.GamehallGame;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomUser;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;

public class IIM implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws Exception {
        String contents = reader.contents();
        String[] commandArgs = reader.contents().split(" ");

        RoomUser roomUser = player.getRoomUser();

        if (roomUser.getRoom() == null) {
            return;
        }

        Room room = roomUser.getRoom();
        Item currentItem = roomUser.getCurrentItem();

        // If we're on a current item and the current item has a valid trigger
        if (currentItem == null) {
            return;
        }

        // If the trigger isn't a game trigger then ignore it
        if (currentItem.getItemTrigger() == null || !(currentItem.getItemTrigger() instanceof GameTrigger)) {
            return;
        }

        GameTrigger trigger = (GameTrigger) currentItem.getItemTrigger();
        GamehallGame game = trigger.getGameInstance(roomUser.getPosition());

        if (game == null) {
            return;
        }

        String gameId = commandArgs[0];
        String command = commandArgs[1];

        if (!gameId.equals(game.getGameId())) {
            return;
        }

        String[] arguments = contents.replace(gameId + " " + command + " ", "").split(" ");
        game.handleCommand(player, room, player.getRoomUser().getCurrentItem(), command, arguments);
    }
}
