package org.alexdev.kepler.game.item;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPublicParser {
    public static List<Item> getPublicItems(String modelId) {
        List<Item> items = new ArrayList<>();
        File file = new File("data" + File.separator + "public_items" + File.separator + modelId + ".dat");

        if (!file.exists()) {
            return items;
        }

        int id = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");

                Item item = new Item();
                item.setId(id++);
                item.getDefinition().getBehaviour().setPublicSpaceObject(true);
                item.getDefinition().setSprite(data[1]);

                item.setCustomData(data[0]);
                item.getPosition().setX(Integer.parseInt(data[2]));
                item.getPosition().setY(Integer.parseInt(data[3]));
                item.getPosition().setZ(Integer.parseInt(data[4]));
                item.getPosition().setRotation(Integer.parseInt(data[5]));

                if (data.length >= 7) {
                    String customData = data[6];

                    if (customData.equals("2")) {
                        item.setHasExtraParameter(true);
                    } else {
                        item.setCurrentProgram(customData);
                    }
                }

                if (item.getDefinition().getSprite().contains("chair")
                        || item.getDefinition().getSprite().contains("bench")
                        || item.getDefinition().getSprite().contains("seat")
                        || item.getDefinition().getSprite().contains("stool")
                        || item.getDefinition().getSprite().contains("sofa")
                        || item.getDefinition().getSprite().equals("l")
                        || item.getDefinition().getSprite().equals("m")
                        || item.getDefinition().getSprite().equals("k")
                        || item.getDefinition().getSprite().equals("shift1")) {
                    item.getDefinition().getBehaviour().setCanSitOnTop(true);
                    item.getDefinition().getBehaviour().setCanStandOnTop(true);
                    item.getDefinition().setTopHeight(1.0);
                } else {
                    item.getDefinition().getBehaviour().setCanSitOnTop(false);
                    item.getDefinition().getBehaviour().setCanStandOnTop(false);
                }

                if (item.getDefinition().getSprite().equals("poolEnter")
                    || item.getDefinition().getSprite().equals("poolExit")
                    || item.getDefinition().getSprite().equals("poolLift")
                    || item.getDefinition().getSprite().equals("poolBooth")
                    || item.getDefinition().getSprite().equals("queue_tile2")) {
                    item.getDefinition().getBehaviour().setCanSitOnTop(false);
                    item.getDefinition().getBehaviour().setCanStandOnTop(true);
                }

                if (item.getDefinition().getSprite().equals("queue_tile2")) {
                    item.getDefinition().getBehaviour().setPublicSpaceObject(false);
                }

                items.add(item);
            }
        } catch (IOException e) {
            return items;
        }

        return items;
    }
}
