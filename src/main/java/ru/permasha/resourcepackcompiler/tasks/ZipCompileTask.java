package ru.permasha.resourcepackcompiler.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.permasha.resourcepackcompiler.Main;
import ru.permasha.resourcepackcompiler.managers.ResourcePackManager;
import ru.permasha.resourcepackcompiler.util.WebServerHandler;
import ru.permasha.resourcepackcompiler.util.ZipFile;

public class ZipCompileTask extends BukkitRunnable {

    Main plugin;

    String pathFolder;
    String pathToZip;

    public ZipCompileTask(Main plugin, String pathFolder, String pathToZip) {
        this.plugin = plugin;
        this.pathFolder = pathFolder;
        this.pathToZip = pathToZip;
    }


    @Override
    public void run() {
        ZipFile.zipFolder(pathFolder, pathToZip);
        Bukkit.getLogger().info("ResourcePack is compiled");
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setResourcePack(ResourcePackManager.getURLResourcePack());
        });
        cancel();
    }
}
