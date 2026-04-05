package a2lib.uncharted;

import a2lib.uncharted.banplus.bancommand;
import a2lib.uncharted.tpa.tpaaccept;
import a2lib.uncharted.tpa.tpacancel;
import a2lib.uncharted.tpa.tpasend;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;

public class a2lib extends JavaPlugin {

    private FileManager fileManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.fileManager = new FileManager(this);
        bancommand banCmd = new bancommand(this);
        registerCommand("banplus", banCmd);
        registerCommand("tempban", banCmd);
        registerCommand("tempmute", banCmd);
        registerCommand("unban", banCmd);
        registerCommand("unmute", banCmd);
        registerCommand("tpa", new tpasend());
        registerCommand("tpaaccept", new tpaaccept());
        registerCommand("tpacancel", new tpacancel());

        getLogger().info("a2lib on");
    }

    @Override
    public void onDisable() {
        if (this.fileManager != null) {
            this.fileManager.saveAll();
        }
        getLogger().info("a2lib off");
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            getLogger().warning("Command '" + name + "' is missing from plugin.yml");
            return;
        }
        command.setExecutor(executor);
    }
}
