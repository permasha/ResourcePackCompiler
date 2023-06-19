package ru.permasha.resourcepackcompiler.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import ru.permasha.resourcepackcompiler.Main;
import ru.permasha.resourcepackcompiler.managers.ResourcePackManager;
import ru.permasha.resourcepackcompiler.util.WebServerHandler;

public class ResourcePackListener implements Listener {

    @EventHandler
    public void ResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        switch (event.getStatus()) {
            case ACCEPTED -> Main.getInstance().getLogger().info(player.getName() + " accepted the resource pack.");
            case SUCCESSFULLY_LOADED -> {
                player.setInvulnerable(false);
                player.setInvisible(false);
                Main.getInstance().getLogger().info(player.getName() + " successfully loaded the resource pack.");
            }
            case DECLINED -> {
                player.setInvulnerable(false);
                player.setInvisible(false);
                Main.getInstance().getLogger().info(player.getName() + " declined the resource pack.");
            }
            case FAILED_DOWNLOAD -> {
                player.setInvulnerable(false);
                player.setInvisible(false);
                Main.getInstance().getLogger().info(player.getName() + " failed to download the resource pack.");
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setResourcePack(ResourcePackManager.getURLResourcePack());
        player.setInvulnerable(true);
        player.setInvisible(true);
    }
}

