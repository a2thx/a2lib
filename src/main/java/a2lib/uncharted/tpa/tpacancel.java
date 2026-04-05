package a2lib.uncharted.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tpacancel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player target = (Player) sender;
        if (args.length < 1) {
            target.sendMessage("§7Usage: /tpacancel <player>");
            return true;
        }

        Player requester = Bukkit.getPlayerExact(args[0]);
        if (requester == null) {
            target.sendMessage("§cThat player is not online.");
            return true;
        }

        tpa request = tpa.getRequest(target, requester);
        if (request == null) {
            target.sendMessage("§cYou do not have a pending TPA request from §7" + requester.getName() + "§c.");
            return true;
        }

        request.deny();
        return true;
    }
}
