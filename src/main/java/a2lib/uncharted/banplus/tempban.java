package a2lib.uncharted.banplus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import a2lib.uncharted.JsonFile;
import a2lib.uncharted.a2lib;
import org.bukkit.ChatColor;

public class tempban {
    private final a2lib plugin;

    public tempban(a2lib plugin) {
        this.plugin = plugin;
    }

    public void ban(OfflinePlayer player, long duration, String reason, String bannedby) {
        long expiryTime = System.currentTimeMillis() + duration;
        Type type = new TypeToken<Map<UUID, tempbanenty>>() {
        }.getType();
        JsonFile<Map<UUID, tempbanenty>> file = plugin.getFileManager().createJsonFile("bans.json", type,
                new HashMap<>());
        Map<UUID, tempbanenty> data = file.getData();
        data.put(player.getUniqueId(), new tempbanenty(reason, bannedby, expiryTime));
        file.save();

        Player onlinePlayer = player.getPlayer();
        if (onlinePlayer != null) {
            onlinePlayer.kickPlayer(ChatColor.RED + "You are banned\nReason: " + reason + "\nBanned by: " + bannedby
                    + "\nFor: " + formatdur(duration));
        }
    }

    private String formatdur(long millis) {
        long seconds = millis / 1000;
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;
        StringBuilder sb = new StringBuilder();
        if (days > 0)
            sb.append(days).append(days == 1 ? " day " : " days ");
        if (hours > 0)
            sb.append(hours).append(hours == 1 ? " hour " : " hours ");
        if (minutes > 0)
            sb.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        if (seconds > 0)
            sb.append(seconds).append(seconds == 1 ? " second" : " seconds");
        String result = sb.toString().trim();
        return result.isEmpty() ? "0 seconds" : result;
    }
}
