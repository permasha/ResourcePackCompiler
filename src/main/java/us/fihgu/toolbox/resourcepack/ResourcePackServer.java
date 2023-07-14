package us.fihgu.toolbox.resourcepack;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;

import com.sun.security.auth.login.ConfigFile;
import ru.permasha.resourcepackcompiler.Main;
import ru.permasha.resourcepackcompiler.util.Debug;
import us.fihgu.toolbox.http.FileContext;
import us.fihgu.toolbox.http.HTTPServer;
import us.fihgu.toolbox.http.StaticContextGenerator;
import us.fihgu.toolbox.network.NetworkUtils;

public class ResourcePackServer {
    public static String localhost;
    public static String host;
    public static int port;

    public static String path;

    private static HTTPServer server;

    public static void startServer() {
        try {
            localhost = Main.getInstance().getConfig().getString("http.localhost",
                    InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            Debug.sayError(
                    "Something just went terribly wrong. Please contact the plugin developer on Spigot and tell him that the plugin couldn't acquire the Localhost IP. If the plugin doesn't work, try adding in the Config, under http, localhost: 0.0.0.0");
            localhost = "127.0.0.1";
        }
        Debug.saySuper("Current Localhost: " + localhost);
        host = Main.getInstance().getConfig().getString("http.host", NetworkUtils.getExternalIP());
        port = Main.getInstance().getConfig().getInt("http.port");
        int numReadThread = Main.getInstance().getConfig().getInt("http.numReadThread");
        int numWriteThread = Main.getInstance().getConfig().getInt("http.numWriteThread");

        int build = Main.getInstance().getConfig().getInt("resource-pack.build", 1);
        path = "/resourcepack" + build + ".zip";

        InetSocketAddress address = new InetSocketAddress(localhost, port);
        server = new HTTPServer(address);
        server.numReadThread = numReadThread;
        server.numWriteThread = numWriteThread;
        server.putContextGenerator(path, new StaticContextGenerator(new FileContext(
                Paths.get(Main.getInstance().getDataFolder() + "/resource-pack/resource.zip"))));

        if (Main.getInstance().getConfig().getBoolean("resource-pack.send-to-player")) {
            if (!Main.getInstance().getConfig().getBoolean("resource-pack.use-minepack")) {
                server.startServer();
            } else {
//                MinePackInitializationMethod.uploadResourcePack(
//                        new File(Main.getInstance().getDataFolder() + "/resource-pack/resource.zip"));
//                Main.getInstance().getConfig().set("resource-pack.sha1", MinePackInitializationMethod.resourcePackHash);
//                Main.getInstance().getConfig().set("resource-pack.link", MinePackInitializationMethod.resourcePack);
            }
        }
    }

    public static void stopServer() {
        if (server != null) {
            server.stopServer();
        }
    }
}
