package a2lib.uncharted;

import org.bukkit.plugin.Plugin;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

/**
 * A manager class to keep track of all JSON files in the plugin.
 */
public class FileManager {

    private final Plugin plugin;
    private final Map<String, JsonFile<?>> files = new HashMap<>();

    public FileManager(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates and loads a new JSON file, or gets an existing one if it's already created.
     * 
     * @param fileName The name of the file (e.g., "bans.json")
     * @param type The TypeToken or Type class of the data
     * @param defaultData What to default to if the file is empty/new
     * @return The JsonFile instance.
     */
    public <T> JsonFile<T> createJsonFile(String fileName, Type type, T defaultData) {
        JsonFile<T> jsonFile = new JsonFile<>(plugin.getDataFolder(), fileName, type, defaultData);
        files.put(fileName, jsonFile);
        return jsonFile;
    }

    /**
     * Retrieve an already-created JsonFile from memory.
     */
    @SuppressWarnings("unchecked")
    public <T> JsonFile<T> getJsonFile(String fileName) {
        return (JsonFile<T>) files.get(fileName);
    }

    /**
     * Convenience method to save all loaded json files at once (e.g., on plugin disable)
     */
    public void saveAll() {
        for (JsonFile<?> file : files.values()) {
            file.save();
        }
    }
}
