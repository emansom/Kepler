package org.alexdev.kepler.game.moderation;

import org.alexdev.kepler.dao.mysql.FuserightsDao;
import org.alexdev.kepler.game.player.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FuserightsManager {
    private static FuserightsManager instance;
    
    private List<String> habboClubFuses;
    private HashMap<String, Integer> fuserights;

    public FuserightsManager() {
        this.fuserights = FuserightsDao.getFuserights();
        this.habboClubFuses = new ArrayList<>();
        this.habboClubFuses.add("fuse_extended_buddylist");
        this.habboClubFuses.add("fuse_habbo_chooser");
        this.habboClubFuses.add("fuse_furni_chooser");
        this.habboClubFuses.add("fuse_room_queue_club");
        this.habboClubFuses.add("fuse_room_priority_access");
        this.habboClubFuses.add("fuse_priority_access");
        this.habboClubFuses.add("fuse_use_special_room_layouts");
        this.habboClubFuses.add("fuse_use_club_dance");
        this.habboClubFuses.add("fuse_use_club_badge");
        this.habboClubFuses.add("fuse_use_club_outfits");
    }

    /**
     * Get the available fuserights for user.
     *
     *
     * @param hasHabboClub whether or not the user has Habbo club
     * @param minimumRank the minimum rank to see the fuseright
     * @return the lsit of fuserights
     */
    public List<String> getAvailableFuserights(boolean hasHabboClub, int minimumRank) {
        List<String> fuses = new ArrayList<>();

        for (var kvp : this.fuserights.entrySet()) {
            if (minimumRank >= kvp.getValue()) {
                fuses.add(kvp.getKey());
            }
        }

        // If we have habbo club, add the habbo club fuserights...
        if (hasHabboClub) {
            fuses.addAll(this.habboClubFuses);
        }

        return fuses;
    }

    /**
     * Get if the rank has a fuseright.
     *
     * @param fuse the fuse to check against
     * @param minimumRank the rank to check with
     * @param hasHabboClub whether or not we have Habbo club, check permissions.
     * @return true, if successful
     */
    public boolean hasFuseright(String fuse, int minimumRank, boolean hasHabboClub) {
        if (hasHabboClub && this.habboClubFuses.contains(fuse)) { // If we have habbo club, check for habbo club fuserights...
            return true;
        }

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
