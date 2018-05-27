package org.alexdev.kepler;

import io.netty.util.ResourceLeakDetector;
import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.GameScheduler;
import org.alexdev.kepler.game.catalogue.CatalogueManager;
import org.alexdev.kepler.game.commands.CommandManager;
import org.alexdev.kepler.game.item.ItemManager;
import org.alexdev.kepler.game.moderation.FuserightsManager;
import org.alexdev.kepler.game.navigator.NavigatorManager;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.game.room.models.RoomModelManager;
import org.alexdev.kepler.game.room.public_rooms.walkways.WalkwaysManager;
import org.alexdev.kepler.game.texts.TextsManager;
import org.alexdev.kepler.messages.MessageHandler;
import org.alexdev.kepler.server.netty.NettyServer;
import org.alexdev.kepler.util.StringUtil;
import org.alexdev.kepler.util.config.Configuration;
import org.alexdev.kepler.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
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
            Configuration.load("config.ini");

            log = LoggerFactory.getLogger(Kepler.class);
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

            // TODO: The "Standard" ASCII from
            // http://patorjk.com/software/taag/#p=display&f=Standard&t=Kepler
            // On a grey background with white ASCII text
            // ASCII logo on the left side, contributors and other credits/info on the right side
            // TODO: also a way to disable the fancy headers, as in production it would only fill the logs
            log.info("Kepler - Habbo Hotel V21 Emulation");

            if (!Storage.connect()) {
                return;
            }

            log.info("Setting up game");

            WalkwaysManager.getInstance();
            ItemManager.getInstance();
            CatalogueManager.getInstance();
            RoomModelManager.getInstance();
            RoomManager.getInstance();
            RoomManager.getInstance().addWalkwaySettings();
            PlayerManager.getInstance();
            FuserightsManager.getInstance();
            NavigatorManager.getInstance();
            GameScheduler.getInstance();
            CommandManager.getInstance();
            MessageHandler.getInstance();
            TextsManager.getInstance();

            // Get the server variables for the socket to listen on
            serverIP = Configuration.getString("server.bind");
            serverPort = Configuration.getInteger("server.port");

            // Override with valid IP that we have resolved
            // TODO: check IPv6 too. And rely on stdlib functions instead of reinventing the wheel
            if (!isValidIpAddress(serverIP)) {
                serverIP = InetAddress.getByName(serverIP).getHostAddress();
            }

            // Create the server instance
            server = new NettyServer(serverIP, serverPort);
            server.createSocket();
            server.bind();

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

            int number = Integer.parseInt(part);

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