package org.alexdev.kepler.game.commands.registered;

import org.alexdev.kepler.game.commands.Command;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.entity.EntityType;
import org.alexdev.kepler.game.moderation.Fuseright;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.user.ALERT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AboutCommand extends Command {

    @Override
    public void addPermissions() {
        this.permissions.add(Fuseright.DEFAULT);
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        List<String> contributorList = new ArrayList<>();
        contributorList.add("Hoshiko");
        contributorList.add("ThuGie");
        contributorList.add("Alito");
        contributorList.add("Ascii");
        contributorList.add("Lightbulb");
        contributorList.add("Raptosaur");
        contributorList.add("<br>  Romuald");
        contributorList.add("Nillus");
        contributorList.add("Holo Team");
        contributorList.add("Meth0d");
        contributorList.add("office.boy");

        StringBuffer about = new StringBuffer();
        about.append("Project Kepler - Habbo Hotel emulation server for Shockwave");
        about.append("<br><br>");
        about.append("Contributors:");
        about.append("<br> - ");
        about.append(String.join(", ", contributorList));
        about.append("<br>");
        about.append("<br><br>Made by Quackster (Alex) from RaGEZONE");

        Player player = (Player)entity;
        player.send(new ALERT(about.toString()));
    }

    @Override
    public String getDescription() {
        return " Information about the software powering this retro";
    }
}
