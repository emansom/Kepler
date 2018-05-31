package org.alexdev.kepler.util.config;

import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameConfiguration {
    private static Map<String, String> config = new ConcurrentHashMap<>();

    public static void load(String configPath) throws IOError, IOException, ConfigurationException {
        setConfigurationDefaults();
        var writer = Configuration.createConfigurationFile(configPath);

        if (writer != null) {
            setConfigurationData(writer);
        }

        config = Configuration.load(configPath);
    }

    private static void setConfigurationDefaults() {
        config.put("fuck.aaron", "true");

        config.put("welcome.message.enabled", "true");
        config.put("welcome.message.content", "Hello, %username%! And welcome to the Kepler server!");

        config.put("roller.tick.default", "6");

        config.put("afk.timer.seconds", "900");
        config.put("sleep.timer.seconds", "300");
    }

    /**
     * Writes default server configuration
     *
     * @param writer - {@link PrintWriter} the file writer
     */
    private static void setConfigurationData(PrintWriter writer) {
        writer.println("[Game]");
        writer.println("fuck.aaron=" + config.get("fuck.aaron"));
        writer.println("");
        writer.println("welcome.message.enabled=" + config.get("welcome.message.enabled"));
        writer.println("welcome.message.content=" + config.get("welcome.message.content"));
        writer.println("");
        writer.println("# 1 tick = 500ms, 6 is 3 seconds");
        writer.println("roller.tick.default=" + config.get("roller.tick.default"));
        writer.println("");
        writer.println("afk.timer.seconds=" + config.get("afk.timer.seconds"));
        writer.println("sleep.timer.seconds=" + config.get("sleep.timer.seconds"));
        writer.println("");
        writer.flush();
        writer.close();
    }


    /**
     * Get key from configuration and cast to an Boolean
     *
     * @param key the key to use
     * @return value as boolean
     */
    public static boolean getBoolean(String key) {
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
    public static String getString(String key) {
        return config.getOrDefault(key, key);
    }

    /**
     * Get value from configuration and cast to an Integer
     *
     * @param key the key to use
     * @return value as int
     */
    public static int getInteger(String key) {
        return Integer.parseInt(config.getOrDefault(key, "0"));
    }
}
