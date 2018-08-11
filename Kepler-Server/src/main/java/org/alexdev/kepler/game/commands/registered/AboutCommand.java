package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.user.ALERT;

public class AboutCommand extends Command {

    @Override
    public void addPermissions() {
        this.permissions.add("default");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player)entity;
        
        player.send(new ALERT("Project Kepler<br><br>Contributors:" +
                "<br> - Hoshiko" + // Aside from myself (Quackster), Hoshiko contributed a lot.
                "<br> - ThuGie" + // Helping me with knowing the Habbo protocol better
                "<br> - Alito" + // Bug tester and contributor for rooms/fixed badges
                "<br> - Ascii" + // Helping me with various packet structures
                "<br> - Lightbulb" + // Helping me with getting tables to show up in public rooms
                "<br> - Raptosaur " + // Call for help
                "<br> - Romuald" + // Creating Habbo Club subscription
                "<br> - Glaceon" + // Creating redeeming furni
                "<br> - Nillus/Holo Team" + // Various parts of Woodpecker, Holograph assisted me, A LOT with protocol
                "<br> - Meth0d" + // Uber had the Club Mammoth furni data
                "<br> - office.boy" + // Parts of Blunk
                "<br><br>Made by Quackster from RaGEZONE"));
    }

    @Override
    public String getDescription() {
        return " Information about the software powering this retro";
    }
}
