package ru.permasha.resourcepackcompiler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.permasha.resourcepackcompiler.commands.PackCommand;
import ru.permasha.resourcepackcompiler.listeners.ResourcePackListener;
import ru.permasha.resourcepackcompiler.managers.ResourcePackManager;
import ru.permasha.resourcepackcompiler.util.WebServerHandler;

import java.io.File;

public final class Main extends JavaPlugin {

    static Main instance;

    private WebServerHandler webServerHandler;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveFolders();

        webServerHandler = new WebServerHandler();
        webServerHandler.start();
        ResourcePackManager.compilePack();
        getCommand("pack").setExecutor(new PackCommand(this));
        registerListeners();
    }

    @Override
    public void onDisable() {
        webServerHandler.stop();
    }

    private void saveFolders() {
        File resource = new File(getDataFolder(), "resource");
        resource.mkdirs();
        File pack = new File(getDataFolder(), "resource/pack");
        pack.mkdirs();
        saveResource("resource/pack/pack.mcmeta", false);
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ResourcePackListener(), this);
    }


    public static Main getInstance() {
        return instance;
    }

    public WebServerHandler getWebServerHandler() {
        return webServerHandler;
    }
}
