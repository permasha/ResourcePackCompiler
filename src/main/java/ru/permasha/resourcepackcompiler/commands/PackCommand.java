package ru.permasha.resourcepackcompiler.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.permasha.resourcepackcompiler.Main;
import us.fihgu.toolbox.resourcepack.ResourcePackListener;
import us.fihgu.toolbox.resourcepack.ResourcePackManager;

import java.io.IOException;

public class PackCommand implements CommandExecutor {

    Main plugin;

    public PackCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!sender.hasPermission("resourcepackcompliler.command.pack")) {
            return true;
        }

       if (args.length != 0) {
           return true;
       }

        try {
            ResourcePackManager.buildResourcePack();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            ResourcePackListener.sendResourcePack(player, player.getAddress().getHostString());
        }

        sender.sendMessage(ChatColor.GREEN + "ResourcePack has been built and sent all players");

        return true;
    }
}
