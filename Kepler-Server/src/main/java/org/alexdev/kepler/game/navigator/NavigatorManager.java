package org.alexdev.kepler.game.navigator;

import org.alexdev.kepler.dao.mysql.NavigatorDao;

import java.util.HashMap;

public class NavigatorManager {
    private static NavigatorManager instance;
    private final HashMap<Integer, NavigatorCategory> categories;

    public NavigatorManager() {
        this.categories = NavigatorDao.getCategories();
    }

    /**
     * Get the map of navigator categories
     *
     * @return the categories
     */
    public HashMap<Integer, NavigatorCategory> getCategories() {
        return categories;
    }

    /**
     * Get instance of {@link NavigatorManager}
     *
     * @return the manager instance
     */
    public static NavigatorManager getInstance() {
        if (instance == null) {
            instance = new NavigatorManager();
        }

        return instance;
    }
}
