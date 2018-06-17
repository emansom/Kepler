package org.alexdev.kepler.util.config;

import org.alexdev.kepler.dao.mysql.SettingsDao;
import org.alexdev.kepler.game.room.RoomManager;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameConfiguration {
    private static GameConfiguration instance;
    private Map<String, String> config;// = new ConcurrentHashMap<>();

    public GameConfiguration() {
        this.config = new ConcurrentHashMap<>();
        this.setConfigurationDefaults();

        for (var entrySet : this.config.entrySet()) {
            String value = SettingsDao.getSetting(entrySet.getKey());

            if (value != null) {
                this.config.put(entrySet.getKey(), value);
            } else {
                SettingsDao.newSetting(entrySet.getKey(), entrySet.getValue());
            }
        }
    }

    private void setConfigurationDefaults() {
        config.put("fuck.aaron", "true");

        config.put("welcome.message.enabled", "false");
        config.put("welcome.message.content", "Hello, %username%! And welcome to the Kepler server!");

        config.put("roller.tick.default", "3000");

        config.put("afk.timer.seconds", "900");
        config.put("sleep.timer.seconds", "300");
        config.put("carry.timer.seconds", "300");
    }

    /**
     * Get key from configuration and cast to an Boolean
     *
     * @param key the key to use
     * @return value as boolean
     */
    public boolean getBoolean(String key) {
        String val = config.getOrDefault(key, "false");

        if (val.equalsIgnoreCase("true")) {
            return true;
        }

        if (val.equals("1")) {
            return true;
        }

        return val.equalsIgnoreCase("yes");

    }

    /**
     * Get value from configuration
     *
     * @param key the key to use
     * @return value
     */
    public String getString(String key) {
        return config.getOrDefault(key, key);
    }

    /**
     * Get value from configuration and cast to an Integer
     *
     * @param key the key to use
     * @return value as int
     */
    public int getInteger(String key) {
        return Integer.parseInt(config.getOrDefault(key, "0"));
    }

    /**
     * Reset all game configuration values.
     */
    public static void reset() {
        instance = null;
        GameConfiguration.getInstance();
    }

    /**
     * Get the instance of {@link GameConfiguration}
     *
     * @return the instance
     */
    public static GameConfiguration getInstance() {
        if (instance == null) {
            instance = new GameConfiguration();
        }

        return instance;
    }
}
