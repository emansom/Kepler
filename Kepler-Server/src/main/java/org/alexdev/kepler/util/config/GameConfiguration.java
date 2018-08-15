package org.alexdev.kepler.util.config;

import org.alexdev.kepler.dao.mysql.CurrencyDao;
import org.alexdev.kepler.dao.mysql.SettingsDao;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameConfiguration {
    private static GameConfiguration instance;
    private Map<String, String> config;

    public GameConfiguration() {
        this.config = new LinkedHashMap<>();
        this.setConfigurationDefaults();

        Map<String, String> settings = SettingsDao.getAllSettings();

        for (var entrySet : this.config.entrySet()) {
            String value = settings.get(entrySet.getKey());

            if (value != null) {
                this.config.put(entrySet.getKey(), value);
            } else {
                SettingsDao.newSetting(entrySet.getKey(), entrySet.getValue());
            }
        }
    }

    private void setConfigurationDefaults() {
        config.put("fuck.aaron", "true");
        config.put("max.connections.per.ip", "2");

        config.put("welcome.message.enabled", "false");
        config.put("welcome.message.content", "Hello, %username%! And welcome to the Kepler server!");

        config.put("roller.tick.default", "2000");

        config.put("afk.timer.seconds", "900");
        config.put("sleep.timer.seconds", "300");
        config.put("carry.timer.seconds", "300");

        config.put("stack.height.limit", "8");
        config.put("roomdimmer.scripting.allowed", "false");

        config.put("credits.scheduler.timeunit", "MINUTES");
        config.put("credits.scheduler.interval", "15");
        config.put("credits.scheduler.amount", "20");

        config.put("chat.garbled.text", "true");
        config.put("chat.bubble.timeout.seconds", "15");

        config.put("tutorial.enabled", "true");
        config.put("profile.editing", "true");
        config.put("vouchers.enabled", "true");

        config.put("shutdown.minutes", "1");

        config.put("rare.cycle.page.id", "2");
        config.put("rare.cycle.refresh.timeunit", "HOURS");
        config.put("rare.cycle.refresh.interval", "2");

        config.put("rare.cycle.reuse.timeunit", "DAYS");
        config.put("rare.cycle.reuse.interval", "3");
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
     * Get value from configuration and cast to a long.
     *
     * @param key the key to use
     * @return value as long
     */
    public long getLong(String key) {
        return Long.parseLong(config.getOrDefault(key, "0"));
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
