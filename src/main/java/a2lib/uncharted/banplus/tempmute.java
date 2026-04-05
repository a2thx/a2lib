package a2lib.uncharted.banplus;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import a2lib.uncharted.JsonFile;
import a2lib.uncharted.a2lib;
import com.google.gson.reflect.TypeToken;

public class tempmute {
    private final a2lib plugin;

    public tempmute(a2lib plugin) {
        this.plugin = plugin;
    }

    public void mute(OfflinePlayer target, long durationMs, String reason, String mutedBy) {
        long expiryTime = System.currentTimeMillis() + durationMs;
        Type type = new TypeToken<Map<UUID, tempmuteenty>>() {}.getType();
        JsonFile<Map<UUID, tempmuteenty>> file = plugin.getFileManager().createJsonFile("mutes.json", type, new HashMap<>());
        
        Map<UUID, tempmuteenty> data = file.getData();
        data.put(target.getUniqueId(), new tempmuteenty(reason, mutedBy, expiryTime));
        file.save();

        Player onlinePlayer = target.getPlayer();
        if (onlinePlayer != null) {
            onlinePlayer.sendMessage("§cYou have been muted for: " + formatDuration(durationMs));
        }
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");

        String result = sb.toString().trim();
        return result.isEmpty() ? "1m" : result;
    }
}
