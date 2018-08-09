package org.alexdev.kepler.game.item;

import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.item.base.ItemDefinition;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ItemPublicParser {
    public static List<Item> getPublicItems(String modelId) {
        List<Item> items = new ArrayList<>();
        File file = Paths.get("tools", "gamedata", "public_items", modelId + ".dat").toFile();

        if (!file.exists()) {
            return items;
        }

        int id = Integer.MAX_VALUE;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");

                Item item = new Item();
                item.setId(id--);
                item.getDefinition().addBehaviour(ItemBehaviour.PUBLIC_SPACE_OBJECT);
                item.getDefinition().setSprite(data[1]);
                item.getDefinition().setTopHeight(ItemDefinition.DEFAULT_TOP_HEIGHT);

                item.setCustomData(data[0]);
                item.getPosition().setX(Integer.parseInt(data[2]));
                item.getPosition().setY(Integer.parseInt(data[3]));
                item.getPosition().setZ(Integer.parseInt(data[4]));
                item.getPosition().setRotation(Integer.parseInt(data[5]));

                if (data.length >= 7) {
                    String customData = data[6];

                    if (customData.equals("2")) {
                        item.getDefinition().addBehaviour(ItemBehaviour.EXTRA_PARAMETER);
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
                        || item.getDefinition().getSprite().equals("shift1")
                        || item.getDefinition().getSprite().equals("stone")
                        || item.getDefinition().getSprite().startsWith("rooftop_flatcurb")) {
                    item.getDefinition().addBehaviour(ItemBehaviour.CAN_SIT_ON_TOP);
                    item.getDefinition().removeBehaviour(ItemBehaviour.CAN_STAND_ON_TOP);
                    item.getDefinition().setTopHeight(1.0);
                } else {
                    item.getDefinition().removeBehaviour(ItemBehaviour.CAN_SIT_ON_TOP);
                    item.getDefinition().removeBehaviour(ItemBehaviour.CAN_STAND_ON_TOP);
                }

                if (item.getDefinition().getSprite().equals("poolEnter")
                    || item.getDefinition().getSprite().equals("poolExit")
                    || item.getDefinition().getSprite().equals("poolLift")
                    || item.getDefinition().getSprite().equals("poolBooth")
                    || item.getDefinition().getSprite().equals("queue_tile2")
                    || item.getDefinition().getSprite().equals("stair")) {
                    //item.getBehaviour().setCanSitOnTop(false);
                    //item.getBehaviour().setCanStandOnTop(true);
                    item.getDefinition().removeBehaviour(ItemBehaviour.CAN_SIT_ON_TOP);
                    item.getDefinition().addBehaviour(ItemBehaviour.CAN_STAND_ON_TOP);
                }

                if (item.getDefinition().getSprite().equals("queue_tile2")) {
                    item.getDefinition().removeBehaviour(ItemBehaviour.PUBLIC_SPACE_OBJECT);
                }

                if (item.getDefinition().getSprite().equals("poolLift") ||
                    item.getDefinition().getSprite().equals("poolBooth")) {
                    item.setCurrentProgramValue("open");
                }

                // Custom heights for these furniture
                if (item.getDefinition().getSprite().equals("picnic_dummychair4")) {
                    item.getDefinition().setTopHeight(4.0);
                }

                if (item.getDefinition().getSprite().equals("picnic_dummychair6")) {
                    item.getDefinition().setTopHeight(7.0);
                }

                // This is the only public item I'm aware of that has a length of 2
                if (item.getDefinition().getSprite().equals("hw_shelf")) {
                    item.getDefinition().setLength(2);
                }

                items.add(item);
            }
        } catch (IOException e) {
            return items;
        }

        return items;
    }
}
