package a2lib.uncharted;

import org.bukkit.plugin.java.JavaPlugin;
import a2lib.uncharted.managers.PluginManager;
import a2lib.uncharted.listeners.PlayerListener;

public class a2lib extends JavaPlugin {
    
    @Override
    public void onEnable() {
        
        // Initialize managers
        PluginManager.getInstance().initialize();
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        
        getLogger().info("a2lib has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("a2lib has been disabled!");
    }
    
}