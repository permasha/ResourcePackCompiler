package ru.permasha.resourcepackcompiler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.permasha.resourcepackcompiler.commands.PackCommand;
import ru.permasha.resourcepackcompiler.util.Debug;
import us.fihgu.toolbox.resourcepack.ResourcePackListener;
import us.fihgu.toolbox.resourcepack.ResourcePackManager;
import us.fihgu.toolbox.resourcepack.ResourcePackServer;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveFolders();

        if (getConfig().getBoolean("resource-pack.force-on-join"))
            ResourcePackManager.setForceResourcePack();

        getCommand("pack").setExecutor(new PackCommand(this));
        registerListeners();

        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> load());
    }

    @Override
    public void onDisable() {
        if (!Main.getInstance().getConfig().getBoolean("resource-pack.use-minepack"))
            ResourcePackServer.stopServer();
    }

    public static void load() {
        setupHTTPServer();
        if (ResourcePackManager.neededRebuild
                && Main.getInstance().getConfig().getBoolean("resource-pack.send-to-player")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ResourcePackListener.sendResourcePack(player, player.getAddress().getHostString());
            }
        }
    }

    private static void setupHTTPServer() {
        try {
            ResourcePackManager.buildResourcePack();
            Debug.sayTrue("Starting an HTTP Server for hosting the Resource Pack.");
            ResourcePackServer.startServer();
            instance.saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveFolders() {
        File resource = new File(getDataFolder(), "resource");
        resource.mkdirs();
        File mcmeta = new File(getDataFolder().getPath() + "/resource", "pack.mcmeta");
        if (!mcmeta.exists()) {
            saveResource("resource/pack.mcmeta", false);
        }
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ResourcePackListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }
}
