package a2lib.uncharted.banplus;

import a2lib.uncharted.JsonFile;
import a2lib.uncharted.a2lib;
import com.google.gson.reflect.TypeToken;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanListener implements Listener {
    private final a2lib plugin;
    private final Type bansType = new TypeToken<Map<UUID, tempbanenty>>() {
    }.getType();

    public BanListener(a2lib plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        JsonFile<Map<UUID, tempbanenty>> file = plugin.getFileManager()
                .createJsonFile("bans.json", bansType, new HashMap<>());
        Map<UUID, tempbanenty> bans = file.getData();
        tempbanenty entry = bans.get(event.getUniqueId());

        if (entry == null) {
            return;
        }

        if (entry.isexpired()) {
            bans.remove(event.getUniqueId());
            file.save();
            return;
        }

        event.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                ChatColor.RED + "You are banned\n"
                        + "Reason: " + entry.getreason() + "\n"
                        + "Banned by: " + entry.getBannedBy() + "\n"
                        + "Time left: " + formatDuration(entry.getExpiryTimeMs() - System.currentTimeMillis())
        );
    }

    private String formatDuration(long millis) {
        long totalSeconds = Math.max(0, millis / 1000);
        long days = totalSeconds / 86400;
        totalSeconds %= 86400;
        long hours = totalSeconds / 3600;
        totalSeconds %= 3600;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(days).append(days == 1 ? " day " : " days ");
        }
        if (hours > 0) {
            builder.append(hours).append(hours == 1 ? " hour " : " hours ");
        }
        if (minutes > 0) {
            builder.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        }
        if (seconds > 0 || builder.length() == 0) {
            builder.append(seconds).append(seconds == 1 ? " second" : " seconds");
        }
        return builder.toString().trim();
    }
}
