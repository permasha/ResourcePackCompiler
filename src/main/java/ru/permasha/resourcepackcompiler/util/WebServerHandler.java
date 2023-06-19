package ru.permasha.resourcepackcompiler.util;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.bukkit.Bukkit;
import ru.permasha.resourcepackcompiler.Main;

import java.io.File;

public class WebServerHandler {

    public int port;
    public String ip;

    private HttpServer httpServer;
    static Main plugin;

    public boolean start() {
        ip = plugin.getServer().getIp();
        if (ip.equals("")) ip = "localhost";
        try {
            port = plugin.getConfig().getInt("http.port");
        } catch (Exception ex) {
            Bukkit.getLogger().info("Invalid port configured in the config.yml");
            return false;
        }
        try {
            httpServer = Vertx.vertx().createHttpServer();
            httpServer.requestHandler(httpServerRequest -> httpServerRequest.response().sendFile(getResourcePackZipFilePath()));
            httpServer.listen(port);
        } catch (Exception ex) {
            Bukkit.getLogger().info("Unable to bind to port. Please assign the plugin to a different port!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    // TODO this does not unassign the web server from the port.
    public void stop() {
        httpServer.close();
    }

    /**
     * Getter path to zip file
     */
    public static String getResourcePackZipFilePath() {
        return plugin.getDataFolder().getPath() + File.separator + "resource" + File.separator + "pack.zip";
    }

    public static File getResourcePackZipFile() {
        return new File(getResourcePackZipFilePath());
    }

    public static String getUnzippedFileLocation() {
        return plugin.getDataFolder().getPath() + File.separator + "resource" + File.separator + "pack";
    }

    public File getWebDirectory() {
        return new File(plugin.getDataFolder().getPath() + File.separator + "resource");
    }
}
