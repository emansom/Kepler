package org.alexdev.kepler.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class StringUtil {

    private static SecureRandom secureRandom;

    static {
        secureRandom = new SecureRandom();
    }

    /**
     * Checks if is null or empty.
     *
     * @param param the param
     * @return true, if is null or empty
     */
    public static boolean isNullOrEmpty(String param) { 
        return param == null || param.trim().length() == 0;
    }

    /**
     * Filter input.
     *
     * @param input the input
     * @param filerNewline if new lines (ENTER) should be filtered
     * @return the string
     */
    public static String filterInput(String input, boolean filerNewline) {
        input = input.replace((char)1, ' ');
        input = input.replace((char)2, ' ');
        input = input.replace((char)9, ' ');
        input = input.replace((char)10, ' ');
        input = input.replace((char)12, ' ');

        if (filerNewline) {
            input = input.replace((char)13, ' ');
        }

        return input;
    }

    /**
     * Checks if is number.
     *
     * @param object the object
     * @return true, if is number
     */
    public static boolean isNumber(Object object) {
        try {
            Integer.valueOf(object.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if is alpha numeric.
     *
     * @param word the word
     * @return true, if is alpha numeric
     */
    public static boolean isAlphaNumeric(String word) {
        return word.matches("[A-Za-z0-9]+");
    }

    /**
     * Removes the non alpha numeric.
     *
     * @param caption the caption
     * @return the string
     */
    public static String removeNonAlphaNumeric(String caption) {
        return caption.replaceAll("[^A-Za-z0-9]", "");
    }

    
    /**
     * Paginate a list of items.
     *
     * @param <T> the generic type
     * @param originalList the original list
     * @param chunkSize the chunk size
     * @return the list
     */
    public static <T> LinkedHashMap<Integer, List<T>> paginate(List<T> originalList, int chunkSize) {
        LinkedHashMap<Integer, List<T>> chunks = new LinkedHashMap<>();
        List<List<T>> listOfChunks = new ArrayList<>();

        for (int i = 0; i < originalList.size() / chunkSize; i++) {
            listOfChunks.add(originalList.subList(i * chunkSize, i * chunkSize + chunkSize));
        }

        if (originalList.size() % chunkSize != 0) {
            listOfChunks.add(originalList.subList(originalList.size() - originalList.size() % chunkSize, originalList.size()));
        }

        for (int i = 0; i < listOfChunks.size(); i++) {
            chunks.put(i, listOfChunks.get(i));
        }

        return chunks;
    }
    
    /**
     * Round to two decimal places.
     *
     * @param decimal the decimal
     * @return the double
     */
    public static double format(double decimal) {
        return Math.round(decimal * 100.0) / 100.0;
    }
    
    /**
     * Split.
     *
     * @param str the string
     * @param delim the delimiter
     * @return the list
     */
    public static List<String> split(String str, String delim) {
        return new ArrayList<>(Arrays.asList(str.split(delim)));
    }

    /**
     * Gets the random.
     *
     * @return the random
     */
    public static SecureRandom getRandom() {
        return secureRandom;
    }
}
