package ru.permasha.resourcepackcompiler.managers;

import org.bukkit.Bukkit;
import ru.permasha.resourcepackcompiler.Main;
import ru.permasha.resourcepackcompiler.tasks.ZipCompileTask;
import ru.permasha.resourcepackcompiler.util.WebServerHandler;
import ru.permasha.resourcepackcompiler.util.ZipFile;

public class ResourcePackManager {
    static Main plugin;

    public static void compilePack() {
        String pathFolder = WebServerHandler.getUnzippedFileLocation();
        String pathToZip = WebServerHandler.getResourcePackZipFilePath();
        new ZipCompileTask(plugin, pathFolder, pathToZip).runTask(plugin);
    }

    public static String getURLResourcePack() {
        return "http://" + plugin.getWebServerHandler().ip + ":" + plugin.getWebServerHandler().port;
    }

}

