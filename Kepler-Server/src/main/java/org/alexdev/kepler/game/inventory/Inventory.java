package org.alexdev.kepler.game.inventory;

import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.inventory.INVENTORY;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private Player player;
    private List<Item> items;
    private int handStripPageIndex;

    public Inventory(Player player) {
        this.player = player;
        this.initialise();
    }

    /**
     * Load inventory items for the user
     */
    private void initialise() {
        this.handStripPageIndex = 0;
        this.items = ItemDao.getInventory(this.player.getDetails().getId());
    }

    /**
     * Get the view of the inventory.
     *
     * @param stripView the view type
     */
    public void getView(String stripView) {
        this.changeView(stripView);

        Map<Integer, Item> casts = this.getCasts(null, this.items.size());
        this.player.send(new INVENTORY(this, casts));
    }

    /**
     * Get the inventory casts for opening hand.
     * Credits to Woodpecker v3 for this, thanks Nillus yet again. <3
     */
    private Map<Integer,Item> getCasts(Map<Integer,Item> casts, int endId) {
        int startId = 0;

        if (casts == null) {
            casts = new HashMap<>();

            if (this.handStripPageIndex == 255) {
                this.handStripPageIndex = ((endId - 1) / 9);
            }
        }

        if (endId > 0) {
            startId = this.handStripPageIndex * 9;

            if (endId > (startId + 9)) {
                endId = startId + 9;
            }

            if (startId >= endId) {
                this.handStripPageIndex--;
                getCasts(casts, endId);
            }

            for (int stripSlotId = startId; stripSlotId < endId; stripSlotId++) {
                Item item = this.items.get(stripSlotId);
                casts.put(stripSlotId, item);
            }
        }

        return casts;
    }

    /**
     * Change the inventory view over.
     *
     * @param stripView the strip view to change
     */
    private void changeView(String stripView) {
        if (stripView.equals("new")) {
            this.handStripPageIndex = 0;
        }

        if (stripView.equals("next")) {
            this.handStripPageIndex++;
        }

        if (stripView.equals("prev")) {
            if (this.handStripPageIndex > 0) {
                this.handStripPageIndex--;
            }
        }

        if (stripView.equals("last")) {
            this.handStripPageIndex++;
        }
    }

    /**
     * Serialise item in hand.
     *
     * @param response the response to write the item to
     * @param item the item to use the data for the packet
     * @param stripSlotId the slot in the hand
     */
    public static void serialise(NettyResponse response, Item item, int stripSlotId) {
        response.writeDelimeter("SI", (char) 30);
        response.writeDelimeter(item.getId(), (char) 30);
        response.writeDelimeter(stripSlotId, (char) 30);

        if (item.hasBehaviour(ItemBehaviour.WALL_ITEM)) {
            response.writeDelimeter("I", (char) 30);
        } else {
            response.writeDelimeter("S", (char) 30);
        }

        response.writeDelimeter(item.getId(), (char) 30);
        response.writeDelimeter(item.getDefinition().getSprite(), (char) 30);

        if (item.hasBehaviour(ItemBehaviour.WALL_ITEM)) {
            response.writeDelimeter(item.getCustomData(), (char) 30);
            response.writeDelimeter("0", (char) 30);
        } else {
            response.writeDelimeter(item.getDefinition().getLength(), (char) 30);
            response.writeDelimeter(item.getDefinition().getWidth(), (char) 30);
            response.writeDelimeter(item.getCustomData(), (char) 30);
            response.writeDelimeter(item.getDefinition().getColour(), (char) 30);
            response.writeDelimeter("0", (char) 30);
            response.writeDelimeter(item.getDefinition().getSprite(), (char) 30);
        }

        response.write("/");
    }

    /**
     * Get inventory item by id.
     *
     * @param itemId the id used to get the inventory item
     * @return the inventory item
     */
    public Item getItem(int itemId) {
        for (Item item : this.items) {
            if (item.getId() == itemId) {
                return item;
            }
        }

        return null;
    }

    /**
     *
     * @return
     */
    public List<Integer> getSoundsets() {
        List<Integer> handSoundsets = new ArrayList<>();

        for (Item item : player.getInventory().getItems()) {
            if (item.hasBehaviour(ItemBehaviour.SOUND_MACHINE_SAMPLE_SET)) {
                handSoundsets.add(Integer.parseInt(item.getDefinition().getSprite().split("_")[2]));
            }
        }

        return handSoundsets;
    }

    /**
     * Get the list of inventory items.
     *
     * @return the list of items
     */
    public List<Item> getItems() {
        return items;
    }
}
