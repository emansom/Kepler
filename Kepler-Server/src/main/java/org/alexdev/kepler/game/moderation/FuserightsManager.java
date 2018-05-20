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

    public List<String> getAvailableFuserights(int minimumRank) {
        List<String> fuses = new ArrayList<>();

        for (Map.Entry<String, Integer> kvp : this.fuserights.entrySet()) {
            if (minimumRank >= kvp.getValue()) {
                fuses.add(kvp.getKey());
            }
        }

        return fuses;
    }

    public static FuserightsManager getInstance() {
        if (instance == null) {
            instance = new FuserightsManager();
        }

        return instance;
    }
}
