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
        config.put("server.ip.address", "127.0.0.1");
        config.put("server.port", "12321");

        config.put("rcon.ip.address", "127.0.0.1");
        config.put("rcon.port", "12309");

        config.put("mysql.hostname", "127.0.0.1");
        config.put("mysql.username", "kepler");
        config.put("mysql.password", "verysecret");
        config.put("mysql.database", "kepler");

        config.put("sso.tickets.enabled", "true");
        config.put("fuck.aaron", "true");

        config.put("welcome.message.enabled", "true");
        config.put("welcome.message.content", "Hello, %username%! And welcome to the Kepler server!");

        config.put("roller.tick.default", "6");

        config.put("debug", "true");
    }

    public static void load(String configPath) throws IOError, IOException, ConfigurationException {
        setDefaults();
        checkLog4j();

        Path path = Paths.get(configPath);

        INIConfiguration ini = new INIConfiguration();

        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        ini.read(reader);

        Set<String> sectionNames = ini.getSections();
        System.out.printf("Section names: %s", sectionNames.toString());

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
    }



/**
   * Create the configuration files for this application, with the default values. Will throw an
   * exception if it could not create such files.
   *
     * @throws IOException
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
//
//	private void writeFileIfNotExist() throws IOException {
//        File file = new File("kepler.properties");
//
//        if (!file.isFile()) {
//            file.createNewFile();
//            PrintWriter writer = new PrintWriter(file.getAbsoluteFile());
//            writeMainConfiguration(writer);
//            writer.flush();
//            writer.close();
//        }
//	}
//
//	/**
//     * Writes default server configuration
//     *
//     * @param writer - {@link PrintWriter} the file writer
//     */
//    private void writeMainConfiguration(PrintWriter writer) {
//        writer.println("[Server]");
//        writer.println("server.ip=127.0.0.1");
//        writer.println("server.port=30001");
//        writer.println();
//        writer.println("[Database]");
//        writer.println("mysql.hostname=127.0.0.1");
//        writer.println("mysql.username=user");
//        writer.println("mysql.password=123");
//        writer.println("mysql.database=kepler");
//        writer.println();
//        writer.println("[Logging]");
//        writer.println("log.connections=true");
//        writer.println("log.sent.packets=false");
//        writer.println("log.received.packets=false");
//        writer.println("log.items.loaded=true");
//        writer.println();
//    }
//
//    /**
//     * Get the main server configuration that controls logging, database access,
//     * and server details.
//     *
//     * @return the configuration
//     */
//    public Wini getServerConfig() {
//        return configuration;
//    }
//
//    public static void init() {
//        instance = new Configuration();
//    }

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
