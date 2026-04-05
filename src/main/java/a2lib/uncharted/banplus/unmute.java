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

public class unmute {
    private final a2lib plugin;

    public unmute(a2lib plugin) {
        this.plugin = plugin;
    }

    public void removeMute(CommandSender sender, OfflinePlayer target) {
        Type type = new TypeToken<Map<UUID, tempmuteenty>>() {}.getType();
        JsonFile<Map<UUID, tempmuteenty>> file = plugin.getFileManager().createJsonFile("mutes.json", type, new HashMap<>());
        
        Map<UUID, tempmuteenty> data = file.getData();

        if (data.containsKey(target.getUniqueId())) {
            data.remove(target.getUniqueId());
            file.save();
            sender.sendMessage("§aSuccessfully unmuted " + target.getName() + "!");
        } else {
            sender.sendMessage("§cThat player is not muted!");
        }
    }
}
