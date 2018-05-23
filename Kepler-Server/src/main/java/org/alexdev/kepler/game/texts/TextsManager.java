package org.alexdev.kepler.game.texts;

import org.alexdev.kepler.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TextsManager {
    private static TextsManager instance;

    private Map<String, String> textsMap;

    public TextsManager() {
        this.textsMap = this.readExternalTexts();
    }

    private Map<String, String> readExternalTexts() {
        Map<String, String> texts = new HashMap<>();

        File file = new File("data" + File.separator + "external_texts.txt");

        if (!file.exists()) {
            return texts;
        }

        int id = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String fileLine;

            while ((fileLine = br.readLine()) != null) {
                if (fileLine.indexOf('=') == -1) {
                    continue;
                }

                String line = StringUtil.filterInput(fileLine, true);

                String key = line.substring(0, line.indexOf('='));
                String value = line.substring(key.length() + 1);

                texts.put(key, value);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return texts;
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
