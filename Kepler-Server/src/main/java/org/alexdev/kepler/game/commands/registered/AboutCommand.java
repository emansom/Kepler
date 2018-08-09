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
        
        player.send(new ALERT("Project Kepler\n\nContributors:" +
                "\n - Hoshiko" + // Aside from myself (Quackster), Hoshiko contributed a lot.
                "\n - ThuGie" + // Helping me with knowing the Habbo protocol better
                "\n - Alito" + // Bug tester and contributor for rooms/fixed badges
                "\n - Ascii" + // Helping me with various packet structures
                "\n - Lightbulb" + // Helping me with getting tables to show up in public rooms
                "\n - Romuald" + // Creating Habbo Club subscription
                "\n - Glaceon" + // Creating redeeming furni
                "\n - Nillus/Holo Team" + // Various parts of Woodpecker, Holograph assisted me, A LOT with protocol
                "\n - Meth0d" + // Uber had the Club Mammoth furni data
                "\n - office.boy" + // Parts of Blunk
                "\n\nMade by Quackster from RaGEZONE"));
    }

    @Override
    public String getDescription() {
        return " Information about the software powering this retro";
    }
}
