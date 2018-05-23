package org.alexdev.kepler.util.config;

import org.alexdev.kepler.log.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.PropertyConfigurator;

public class Configuration {
    private static Map<String, String> config = new ConcurrentHashMap<>();

    public static void setDefaults() {
        // Default settings
        config.put("server.bind", "127.0.0.1");
        config.put("server.port", "12321");

        config.put("rcon.bind", "127.0.0.1");
        config.put("rcon.port", "12309");

        config.put("log.sent.packets", "false");
        config.put("log.received.packets", "false");

        config.put("mysql.hostname", "127.0.0.1");
        config.put("mysql.username", "kepler");
        config.put("mysql.password", "verysecret");
        config.put("mysql.database", "kepler");

        config.put("sso.tickets.enabled", "true");
        config.put("fuck.aaron", "true");

        config.put("welcome.message.enabled", "true");
        config.put("welcome.message.content", "Hello, %username%! And welcome to the Kepler server!");

        config.put("roller.tick.default", "6");

        config.put("debug", "false");
    }

    public static void load(String configPath) throws IOError, IOException, ConfigurationException {
        setDefaults();
        checkLog4j();
        writeFileIfNotExist();

        Path path = Paths.get(configPath);

        INIConfiguration ini = new INIConfiguration();

        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        ini.read(reader);

        Set<String> sectionNames = ini.getSections();
        //System.out.printf("Section names: %s", sectionNames.toString());

        for (String sectionName : sectionNames) {
            SubnodeConfiguration section = ini.getSection(sectionName);

            if (section != null) {
                Iterator<String> keys = section.getKeys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = section.getString(key);

                    if (value != null) {
                        key = key.replace("..", "."); // TODO: find a better way than this hack
                        config.put(key, value);
                    }
                }
            }
        }

        reader.close();
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

    /**
     * Create the configuration files for this application, with the default values. Will throw an
     * exception if it could not create such files.
     *
     * @throws FileNotFoundException the exception if an error happens
     */
    private static void checkLog4j() throws FileNotFoundException {

        String output = "log4j.rootLogger=INFO, stdout, SERVER_LOG\n" +
                "log4j.appender.stdout.threshold=info\n" +
                "log4j.appender.stdout=org.apache.log4j.ConsoleAppender\n" +
                "log4j.appender.stdout.Target=System.out\n" +
                "log4j.appender.stdout.layout=org.apache.log4j.PatternLayout\n" +
                "log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd'T'HH:mm:ss.SSS} %-5p [%c] - %m%n\n" +
                "\n" +
                "# Create new logger information for error\n" +
                "log4j.logger.ErrorLogger=ERROR, error, ERROR_FILE\n" +
                "log4j.additivity.ErrorLogger=false\n" +
                "\n" +
                "# Set settings for the error logger\n" +
                "log4j.appender.error=org.apache.log4j.ConsoleAppender\n" +
                "log4j.appender.error.Target=System.err\n" +
                "log4j.appender.error.layout=org.apache.log4j.PatternLayout\n" +
                "log4j.appender.error.layout.ConversionPattern=%d{yyyy-MM-dd'T'HH:mm:ss.SSS} %-5p [%c] - %m%n\n" +
                "\n" +
                "# Define the file appender for errors\n" +
                "log4j.appender.ERROR_FILE=org.apache.log4j.FileAppender\n" +
                "log4j.appender.ERROR_FILE.File=error.log\n" +
                "log4j.appender.ERROR_FILE.ImmediateFlush=true\n" +
                "log4j.appender.ERROR_FILE.Threshold=debug\n" +
                "log4j.appender.ERROR_FILE.Append=true\n" +
                "log4j.appender.ERROR_FILE.layout=org.apache.log4j.PatternLayout\n" +
                "log4j.appender.ERROR_FILE.layout.conversionPattern=%d{yyyy-MM-dd'T'HH:mm:ss.SSS} - [%c] - %m%n\n" +
                "\n" +
                "# Define the file appender for server output\n" +
                "log4j.appender.SERVER_LOG=org.apache.log4j.FileAppender\n" +
                "log4j.appender.SERVER_LOG.File=server.log\n" +
                "log4j.appender.SERVER_LOG.ImmediateFlush=true\n" +
                "log4j.appender.SERVER_LOG.Threshold=debug\n" +
                "log4j.appender.SERVER_LOG.Append=true\n" +
                "log4j.appender.SERVER_LOG.layout=org.apache.log4j.PatternLayout\n" +
                "log4j.appender.SERVER_LOG.layout.conversionPattern=%d{yyyy-MM-dd'T'HH:mm:ss.SSS} - [%c] - %m%n\n";

        File loggingConfig = new File("log4j.properties");

        if (!loggingConfig.exists()) {
            PrintWriter writer = new PrintWriter(loggingConfig.getAbsoluteFile());
            writer.write(output);
            writer.flush();
            writer.close();
        }

        //Change the path where the logger property should be read from
        PropertyConfigurator.configure(loggingConfig.getAbsolutePath());
    }

    /**
     * Create config file
     * @throws IOException the exception if the file couldn't be read/written to
     */
    private static void writeFileIfNotExist() throws IOException {
        File file = new File("config.ini");

        if (!file.isFile() && file.createNewFile()) {
            PrintWriter writer = new PrintWriter(file.getAbsoluteFile());
            writeMainConfiguration(writer);
            writer.flush();
            writer.close();
        }
    }

    /**
     * Writes default server configuration
     *
     * @param writer - {@link PrintWriter} the file writer
     */
    private static void writeMainConfiguration(PrintWriter writer) {
        writer.println("[Server]");
        writer.println("server.bind=" + config.get("server.bind"));
        writer.println("server.port=" + config.get("server.port"));
        writer.println("");
        writer.println("[Rcon]");
        writer.println("rcon.bind=" + config.get("rcon.bind"));
        writer.println("rcon.port=" + config.get("rcon.port"));
        writer.println("");
        writer.println("[Database]");
        writer.println("mysql.hostname=" + config.get("mysql.hostname"));
        writer.println("mysql.username=" + config.get("mysql.username"));
        writer.println("mysql.password=" + config.get("mysql.password"));
        writer.println("mysql.database=" + config.get("mysql.database"));
        writer.println("");
        writer.println("[Logging]");
        writer.println("log.received.packets=" + config.get("log.received.packets"));
        writer.println("log.sent.packets=" + config.get("log.sent.packets"));
        writer.println("");
        writer.println("[Game]");
        writer.println("sso.tickets.enabled=" + config.get("sso.tickets.enabled"));
        writer.println("fuck.aaron=true" + config.get("fuck.aaron"));
        writer.println("");
        writer.println("welcome.message.enabled=" + config.get("welcome.message.enabled"));
        writer.println("welcome.message.content=" + config.get("welcome.message.content"));
        writer.println("");
        writer.println("# 1 tick = 500ms, 6 is 3 seconds");
        writer.println("roller.tick.default=" + config.get("roller.tick.default"));
        writer.println("");
        writer.println("[Console]");
        writer.println("debug=" + config.get("debug"));
        writer.println("");
    }
}
