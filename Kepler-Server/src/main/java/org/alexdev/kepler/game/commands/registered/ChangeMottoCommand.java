package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.rooms.user.CHAT_MESSAGE;
import org.alexdev.kepler.messages.outgoing.rooms.user.FIGURE_CHANGE;
import org.alexdev.kepler.util.StringUtil;

public class ChangeMottoCommand extends Command {
    @Override
    public void addPermissions() {
        this.permissions.add("default");
    }

    @Override
    public void addArguments() {
        this.arguments.add("motto");
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

        if (args.length == 0) {
            player.send(new CHAT_MESSAGE(CHAT_MESSAGE.type.WHISPER, player.getRoomUser().getInstanceId(), "No motto provided"));
            return;
        }

        // Filter out possible packet injection attacks
        String motto = StringUtil.filterInput(args[0], true);

        // Update motto
        player.getDetails().setMotto(motto);
        PlayerDao.saveMotto(player.getDetails());

        // Notify room of changed motto
        player.getRoomUser().getRoom().send(new FIGURE_CHANGE(player.getRoomUser().getInstanceId(), player.getDetails()));
    }

    @Override
    public String getDescription() {
        return "Change your motto";
    }
}
