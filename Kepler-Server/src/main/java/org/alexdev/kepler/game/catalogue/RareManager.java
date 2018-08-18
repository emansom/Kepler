package org.alexdev.kepler.game.catalogue;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.dao.mysql.RareDao;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.config.GameConfiguration;
import org.bouncycastle.asn1.cms.Time;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class RareManager {
    private static RareManager instance;

    private LinkedList<CatalogueItem> rareList;
    private Map<String, Long> daysSinceUsed;

    private CatalogueItem currentRare;
    private Long currentRareTime;

    public RareManager() {
        try {
            this.daysSinceUsed = RareDao.getActiveBlockedRares();

            if (this.daysSinceUsed.size() > 0) {
                var currentItemData = RareDao.getCurrentRare();
                this.currentRare = CatalogueManager.getInstance().getCatalogueItem(currentItemData.getKey());
                this.currentRareTime = currentItemData.getValue(); // Get the active item
            }

            this.loadRares();

            TimeUnit reuseTimeUnit = TimeUnit.valueOf(GameConfiguration.getInstance().getString("rare.cycle.reuse.timeunit"));
            long reuseInterval = GameConfiguration.getInstance().getInteger("rare.cycle.reuse.interval");

            TimeUnit refreshTimeUnit = TimeUnit.valueOf(GameConfiguration.getInstance().getString("rare.cycle.refresh.timeunit"));
            long refreshInterval = GameConfiguration.getInstance().getInteger("rare.cycle.refresh.interval");

            long refreshTime = -1;

            if (this.currentRare != null) {
                long reuseInSeconds = reuseTimeUnit.toSeconds(reuseInterval);
                long refreshInSeconds = refreshTimeUnit.toSeconds(refreshInterval);

                refreshTime = (this.currentRareTime - reuseInSeconds) + refreshInSeconds;
            }

            // If there was no current rare, or the current rare time ran out, then cycle to the next rare
            if (this.currentRare == null || (this.currentRare != null && (DateUtil.getCurrentTimeSeconds() > refreshTime))) {
                this.selectNewRare();
            }
        } catch (Exception ex) {
            Storage.logError(ex);
        }
    }

    /**
     * Finds all rares that can't be accessed normally by the user, and then shuffles the list.
     */
    private void loadRares() {
        this.rareList = new LinkedList<>();

        for (CataloguePage cataloguePage : CatalogueManager.getInstance().getCataloguePages()) {
            // Skip pages where normal users can access
            if (!(cataloguePage.getMinRole() > 1)) {
                continue;
            }

            // Search in rares pages only
            if (!cataloguePage.getLayout().equals("ctlg_layout2") || !cataloguePage.getImageHeadline().equals("catalog_rares_headline1")) {
                continue;
            }

            this.rareList.addAll(CatalogueManager.getInstance().getCataloguePageItems(cataloguePage.getId()));
        }

        Collections.shuffle(this.rareList);
    }

    /**
     * Selects a new rare, adds it to the database so it can only be selected once every X interval defined (default is 3 days).
     */
    public void selectNewRare() throws SQLException {
        TimeUnit reuseTimeUnit = TimeUnit.valueOf(GameConfiguration.getInstance().getString("rare.cycle.reuse.timeunit"));
        long interval = reuseTimeUnit.toSeconds(GameConfiguration.getInstance().getInteger("rare.cycle.reuse.interval"));

        List<String> toRemove = new ArrayList<>();

        // Remove expired rares
        for (var kvp : this.daysSinceUsed.entrySet()) {
            if (DateUtil.getCurrentTimeSeconds() > kvp.getValue()) {
                toRemove.add(kvp.getKey());
            }
        }

        for (var sprite : toRemove) {
            this.daysSinceUsed.remove(sprite);
        }

        RareDao.removeRares(toRemove);

        // If the rare list has ran out, reload it.
        if (this.rareList.isEmpty()) {
            this.loadRares();
        }

        CatalogueItem rare = this.rareList.pollFirst(); // Select the rare from the rare list

        if (rare != null) {
            // If the rare is in the expired list, search for another rare
            if (this.daysSinceUsed.containsKey(rare.getDefinition().getSprite())) {
                this.currentRare = null; // Set to null in case we can't find one, so it can default back to the default catalogue item set in database

                if (this.rareList.size() > 0) {
                    this.selectNewRare();
                }

                return;
            }

            this.currentRare = rare;

            // Handle override by using "rare.cycle.reuse.CATALOGUE_SALE_CODE.timeunit" and "rare.cycle.reuse.CATALOGUE_SALE_CODE.interval"
            String overrideUnit = GameConfiguration.getInstance().getString("rare.cycle.reuse." + rare.getSaleCode() + ".timeunit", null);

            if (overrideUnit != null) {
                reuseTimeUnit = TimeUnit.valueOf(overrideUnit);
                interval = reuseTimeUnit.toSeconds(GameConfiguration.getInstance().getInteger("rare.cycle.reuse." + rare.getSaleCode() + ".interval"));
            }

            // Add rare to expiry table so it can't be used for a certain X number of days
            this.daysSinceUsed.put(rare.getDefinition().getSprite(), DateUtil.getCurrentTimeSeconds() + interval);

            RareDao.removeRares(List.of(rare.getDefinition().getSprite()));
            RareDao.addRare(rare.getDefinition().getSprite(), DateUtil.getCurrentTimeSeconds() + interval);
        }
    }

    /**
     * Remove the colour tag from the sprite name. eg pillow*1 to pillow, used for
     * comparing the same items which are just different colours.
     *
     * @param sprite the sprite to remove the colour tag from
     * @return the new sprite
     */
    private String stripColor(String sprite) {
        return sprite.contains("*") ? sprite.split("\\*")[0] : sprite;
    }

    /**
     * Get the current random rare
     * @return the random rare
     */
    public CatalogueItem getCurrentRare() {
        return currentRare;
    }

    /**
     * Get the {@link RareManager} instance
     *
     * @return the rare manager instance
     */
    public static RareManager getInstance() {
        if (instance == null) {
            instance = new RareManager();
        }

        return instance;
    }
}
