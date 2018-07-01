package org.alexdev.kepler.game.room.tasks;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.pathfinder.Pathfinder;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.mapping.RoomTile;
import org.alexdev.kepler.messages.outgoing.rooms.items.SLIDE_OBJECT;
import org.alexdev.kepler.util.config.GameConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RainbowTask implements Runnable {
    private final Room room;
    private int colourIndex;

    private static Map<Integer, String> HEX_COLOURS;

    public RainbowTask(Room room) {
        if (HEX_COLOURS == null) {
            HEX_COLOURS = new LinkedHashMap<>();

            double frequency = 0.5;

            for (var i = 0; i < 32; ++i) {
                double red = Math.sin(frequency*i + 0) * 127 + 128;
                double green = Math.sin(frequency*i + 2) * 127 + 128;
                double blue = Math.sin(frequency*i + 4) * 127 + 128;

                String hex = String.format("#%02x%02x%02x", (int)red, (int)green, (int)blue);
                HEX_COLOURS.put(i, hex);
            }
        }

        this.room = room;
        this.colourIndex = -1;
    }

    @Override
    public void run() {
        Item moodlight = this.room.getItemManager().getMoodlight();

        if (moodlight == null) {
            this.room.getTaskManager().cancelCustomTask("RainbowTask");
            return;
        }

        this.colourIndex++;

        if (!HEX_COLOURS.containsKey(this.colourIndex)) {
            this.colourIndex = 0;
        }

        String hexColour = HEX_COLOURS.get(this.colourIndex);

        // 2,1,1,#0053F7,211
        // enable moodlight, preset id, background state, hex colour, strength (middle)

        String newCustomData = "2,1,1," + hexColour + ",211";

        moodlight.setCustomData(newCustomData);
        moodlight.updateStatus();
    }
}
