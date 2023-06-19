package ru.permasha.resourcepackcompiler.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.permasha.resourcepackcompiler.Main;
import ru.permasha.resourcepackcompiler.managers.ResourcePackManager;

public class PackCommand implements CommandExecutor {

    Main plugin;

    public PackCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!sender.hasPermission("qbengine.command.pack")) {
            return true;
        }

       if (args.length != 0) {
           return true;
       }

       ResourcePackManager.compilePack();

        return true;
    }
}
