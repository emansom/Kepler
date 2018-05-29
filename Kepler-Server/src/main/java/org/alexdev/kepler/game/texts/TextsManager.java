package org.alexdev.kepler.game.texts;

import org.alexdev.kepler.util.StringUtil;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TextsManager {
    private static TextsManager instance;

    private Map<String, String> textsMap;

    public TextsManager() {
        this.textsMap = new HashMap<>();
        this.readExternalTexts();
    }

    private void readExternalTexts() {
        try {
            INIConfiguration ini = new INIConfiguration();
            BufferedReader reader = reader = Files.newBufferedReader(
                    Paths.get("data", "external_texts.txt"),
                    StandardCharsets.UTF_8);

            ini.read(reader);

            Set<String> sectionNames = ini.getSections();

            for (String sectionName : sectionNames) {
                SubnodeConfiguration section = ini.getSection(sectionName);

                if (section != null) {
                    Iterator<String> keys = section.getKeys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = section.getString(key);

                        if (value != null) {
                            key = key.replace("..", "."); // TODO: find a better way than this hack
                            this.textsMap.put(key, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a external text value by key
     *
     * @param key the external text key to get
     * @return the external text value
     */
    public String getValue(String key) {
        if (this.textsMap.containsKey(key)) {
            return this.textsMap.get(key);
        }

        return "";
    }

    /**
     * Get the {@link TextsManager} instance
     *
     * @return the item manager instance
     */
    public static TextsManager getInstance() {
        if (instance == null) {
            instance = new TextsManager();
        }

        return instance;
    }
}
