package us.fihgu.toolbox.resourcepack;

import com.sun.security.auth.login.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import ru.permasha.resourcepackcompiler.Main;
import ru.permasha.resourcepackcompiler.util.Debug;

public class ResourcePackListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        sendResourcePack(event.getPlayer(), event.getAddress().getHostAddress());
    }

    public static void sendResourcePack(Player player, String hostAddress) {
        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
            if (Main.getInstance().getConfig().getBoolean("resource-pack.send-to-player")) {
                String link = "";
                if (!Main.getInstance().getConfig().getBoolean("resource-pack.use-minepack")) {
                    if (hostAddress != null && hostAddress.equals("127.0.0.1")) {
                        link = "http://" + ResourcePackServer.localhost + ":" + ResourcePackServer.port
                                + ResourcePackServer.path;
                    } else {
                        link = "http://" + ResourcePackServer.host + ":" + ResourcePackServer.port
                                + ResourcePackServer.path;
                    }
                } //else {
//                    link = MinePackInitializationMethod.resourcePack;
                // }
                if (player != null && player.isOnline())
                    if (ResourcePackManager.hasSendWithHash)
                        try {
                            player.setResourcePack(link, ResourcePackManager.resourcePackSha1Byte);
                        } catch (NoSuchMethodError e) {
                            ResourcePackManager.hasSendWithHash = false;
                            player.setResourcePack(link);
                        }
                    else
                        player.setResourcePack(link);

                Debug.saySuper("Sending Resource Pack Link to Player: " + link);
            }
        }, 20L);
    }

    @EventHandler
    public void onResourcestatusChange(PlayerResourcePackStatusEvent event) {
        if (ResourcePackManager.getForceResourcePack()) {
            Status status = event.getStatus();
            switch (status) {
                case DECLINED:
                case FAILED_DOWNLOAD:
                    final Player player = event.getPlayer();
                    Bukkit.getServer().getScheduler().runTask(Main.getInstance(),
                            () -> player.kickPlayer("Ошибка загрузки!"));
                    break;
                case ACCEPTED:
                case SUCCESSFULLY_LOADED:
                default:
                    break;
            }
        }
    }
}