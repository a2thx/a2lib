package a2lib.uncharted.banplus;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import a2lib.uncharted.JsonFile;
import a2lib.uncharted.a2lib;
import com.google.gson.reflect.TypeToken;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class unban {
    private final a2lib plugin;

    public unban(a2lib plugin) {
        this.plugin = plugin;
    }

    public void removeBan(CommandSender sender, OfflinePlayer target) {
        Type type = new TypeToken<Map<UUID, tempbanenty>>() {
        }.getType();
        JsonFile<Map<UUID, tempbanenty>> file = plugin.getFileManager().createJsonFile("bans.json", type,
                new HashMap<>());
        Map<UUID, tempbanenty> data = file.getData();
        if (data.containsKey(target.getUniqueId())) {
            data.remove(target.getUniqueId());
            file.save();
            sender.sendMessage("§aSuccessfully unbanned " + target.getName() + "!");
        } else {
            sender.sendMessage("§cThat player is not banned!");
        }
    }
}
