package org.alexdev.kepler;

import io.netty.util.ResourceLeakDetector;
import org.alexdev.kepler.console.ConsoleReader;
import org.alexdev.kepler.dao.mysql.Storage;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.commands.CommandManager;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.server.netty.NettyServer;
import org.alexdev.kepler.util.StringUtil;
import org.alexdev.kepler.util.config.Configuration;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.config.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class Kepler {

    private static long startupTime;

    private static String serverIP;
    private static int serverPort;
    
    private static NettyServer server;
    private static Logger log;

    /**
     * Main call of Java application
     * @param args System arguments
     */
    public static void main(String[] args) {
        startupTime = DateUtil.getCurrentTimeSeconds();

        try {
            Configuration.getInstance();
            Locale.getInstance();

            log = LoggerFactory.getLogger(Kepler.class);
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

            // The "Doom" ASCII from
            // http://patorjk.com/software/taag/#p=display&f=Doom&t=Icarus
            log.info("Kepler - Habbo Hotel V21 Emulation");
            log.info("Loading server...");
            log.info("");

            if (!Storage.connect()) {
                return;
            }

            log.info("Setting up game");

            GameScheduler.getInstance();
            CommandManager.getInstance();
            PlayerManager.getInstance();

            log.info("Setting up server");

            // Get the server variables for the socket to listen on
            serverIP = Configuration.getInstance().getServerConfig().get("Server", "server.ip", String.class);
            serverPort = Configuration.getInstance().getServerConfig().get("Server", "server.port", int.class);

            // Override with valid IP that we have resolved
            if (!isValidIpAddress(serverIP)) {
                serverIP = InetAddress.getByName(serverIP).getHostAddress();
            }

            // Create the server instance
            server = new NettyServer(serverIP, serverPort);
            server.createSocket();
            server.bind();

            // Create console reader
            ConsoleReader.getInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate IP address that the server attempts to listen to
     * 
     * @param ip the ipv4
     * @return true if valid IPv4
     */
    public static boolean isValidIpAddress(String ip) {
        String[] numbers = ip.split("\\.");

        if (numbers.length != 4) {
            return false;
        }

        for (String part : numbers) {
            if (!StringUtil.isNumber(part)) {
                return false;
            }

            int number = Integer.valueOf(part);

            if (number > 255 || number < 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the interface to the server handler
     * 
     * @return {@link NettyServer} interface
     */
    public static NettyServer getServer() {
        return server;
    }

    /**
     * Gets the server IPv4 IP address it is currently (or attempting to) listen on
     * @return IP as string
     */
    public static String getServerIP() {
        return serverIP;
    }

    /**
     * Gets the server port it is currently (or attempting to) listen on
     * @return string of IP
     */
    public static int getServerPort() {
        return serverPort;
    }

    /**
     * Gets the startup time.
     *
     * @return the startup time
     */
    public static long getStartupTime() {
        return startupTime;
    }
}