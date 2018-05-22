package org.alexdev.kepler.game.moderation;

import org.alexdev.kepler.dao.mysql.FuserightsDao;
import org.alexdev.kepler.game.player.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FuserightsManager {
    private static FuserightsManager instance;
    private HashMap<String, Integer> fuserights;

    public FuserightsManager() {
        this.fuserights = FuserightsDao.getFuserights();
    }

    /**
     * Get the available fuserights for user.
     *
     * @param minimumRank the minimum rank to see the fuseright
     * @return the lsit of fuserights
     */
    public List<String> getAvailableFuserights(int minimumRank) {
        List<String> fuses = new ArrayList<>();

        for (var kvp : this.fuserights.entrySet()) {
            if (minimumRank >= kvp.getValue()) {
                fuses.add(kvp.getKey());
            }
        }

        return fuses;
    }

    /**
     * Get if the rank has a fuseright.
     *
     * @param fuse the fuse to check against
     * @param minimumRank the rank to check with
     * @return true, if successful
     */
    public boolean hasFuseright(String fuse, int minimumRank) {
        for (var kvp : this.fuserights.entrySet()) {
            if (minimumRank >= kvp.getValue() && kvp.getKey().equals(fuse)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the fuseright manager instance.
     *
     * @return the fuseright manager
     */
    public static FuserightsManager getInstance() {
        if (instance == null) {
            instance = new FuserightsManager();
        }

        return instance;
    }
}
