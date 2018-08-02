package org.alexdev.kepler;

import com.goterl.lazycode.lazysodium.interfaces.PwHash;
import io.netty.util.ResourceLeakDetector;
import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.dao.mysql.PlayerDao;
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
import org.alexdev.kepler.server.mus.MusServer;
import org.alexdev.kepler.server.mus.connection.MusClient;
import org.alexdev.kepler.server.netty.NettyServer;
import org.alexdev.kepler.server.rcon.RconServer;
import org.alexdev.kepler.util.config.GameConfiguration;
import org.alexdev.kepler.util.config.ServerConfiguration;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.config.LoggingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Kepler {

    private static long startupTime;

    private static String serverIP;
    private static int serverPort;

    private static String musServerIP;
    private static int musServerPort;

    private static String rconIP;
    private static int rconPort;

    private static boolean isShutdown;
    
    private static NettyServer server;
    private static MusServer musServer;
    private static RconServer rcon;

    private static Logger log;

    /**
     * Main call of Java application
     * @param args System arguments
     */
    public static void main(String[] args) {
        startupTime = DateUtil.getCurrentTimeSeconds();

        try {
            LoggingConfiguration.checkLoggingConfig();
            ServerConfiguration.load("server.ini");

            /*byte[] pw = "lol".getBytes(StandardCharsets.UTF_8);
            byte[] outputHash = new byte[PwHash.STR_BYTES];
            PwHash.Native pwHash = (PwHash.Native) PlayerDao.LIB_SODIUM;

            boolean success = pwHash.cryptoPwHashStr(
                    outputHash,
                    pw,
                    pw.length,
                    PwHash.OPSLIMIT_INTERACTIVE,
                    PwHash.MEMLIMIT_INTERACTIVE
            );

            System.out.println(new String(outputHash));*/

            log = LoggerFactory.getLogger(Kepler.class);
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

            // TODO: The "Standard" ASCII from
            // http://patorjk.com/software/taag/#p=display&f=Standard&t=Kepler

            System.out.println("  _  __          _           \n" +
                    " | |/ /___ _ __ | | ___ _ __ \n" +
                    " | ' // _ \\ '_ \\| |/ _ \\ '__|\n" +
                    " | . \\  __/ |_) | |  __/ |   \n" +
                    " |_|\\_\\___| .__/|_|\\___|_|   \n" +
                    "          |_|                ");

            log.info("Kepler - Habbo Hotel V21 Emulation");

            if (!Storage.connect()) {
                return;
            }

            log.info("Setting up game");

            GameConfiguration.getInstance();
            WalkwaysManager.getInstance();
            ItemManager.getInstance();
            CatalogueManager.getInstance();
            RoomModelManager.getInstance();
            RoomManager.getInstance();
            PlayerManager.getInstance();
            FuserightsManager.getInstance();
            NavigatorManager.getInstance();
            GameScheduler.getInstance();
            CommandManager.getInstance();
            MessageHandler.getInstance();
            TextsManager.getInstance();

            // Get an InetAddressValidator
            InetAddressValidator validator = InetAddressValidator.getInstance();

            setupServer(validator);
            setupRcon(validator);
            setupMus(validator);

            Runtime.getRuntime().addShutdownHook(new Thread(Kepler::dispose));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setupServer(InetAddressValidator validator) throws UnknownHostException {
        serverIP = ServerConfiguration.getString("server.bind");
        serverPort = ServerConfiguration.getInteger("server.port");

        if (!validator.isValid(serverIP)) {
            log.error("%s is not a valid server IP", serverIP);
            return;
        }

        serverIP = InetAddress.getByName(serverIP).getHostAddress();

        server = new NettyServer(serverIP, serverPort);
        server.createSocket();
        server.bind();
    }

    private static void setupRcon(InetAddressValidator validator) throws IOException {
        // Create the RCON instance
        rconIP = ServerConfiguration.getString("rcon.bind");
        rconPort = ServerConfiguration.getInteger("rcon.port");

        // Validate an IPv4 or IPv6 address
        if (!validator.isValid(rconIP)) {
            log.error("%s is not a valid IP", rconIP);
            return;
        }

        // getByName parses IPv6, IPv4 and DNS all in one go
        rconIP = InetAddress.getByName(rconIP).getHostAddress();

        rcon = new RconServer(rconIP, rconPort);
        rcon.listen();
    }

    private static void setupMus(InetAddressValidator validator) throws UnknownHostException {
        musServerIP = ServerConfiguration.getString("mus.bind");
        musServerPort = ServerConfiguration.getInteger("mus.port");

        if (musServerIP.length() > 0) {
            if (!validator.isValid(musServerIP)) {
                log.error("%s is not a valid IP", musServerIP);
                return;
            }

            musServerIP = InetAddress.getByName(musServerIP).getHostAddress();

            musServer = new MusServer(musServerIP, musServerPort);
            musServer.createSocket();
            musServer.bind();
        }
    }

    private static void dispose() {
        try {

            log.info("Shutting down server!");
            isShutdown = true;

            // TODO: all the managers
            PlayerManager.getInstance().dispose();

            server.dispose();
            rcon.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Returns the interface to the server handler
     *
     * @return {@link NettyServer} interface
     */
    public static RconServer getRcon() {
        return rcon;
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
     * Gets the rcon IPv4 IP address it is currently (or attempting to) listen on
     * @return IP as string
     */
    public static String getRconIP() {
        return rconIP;
    }

    /**
     * Gets the rcon port it is currently (or attempting to) listen on
     * @return string of IP
     */
    public static int getRconPort() {
        return rconPort;
    }

    /**
     * Gets the startup time.
     *
     * @return the startup time
     */
    public static long getStartupTime() {
        return startupTime;
    }

    /**
     * Are we shutting down?
     *
     * @return boolean yes/no
     */
    public static boolean getIsShutdown() {
        return isShutdown;
    }
}