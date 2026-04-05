package a2lib.uncharted.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tpasend implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§7Usage: /tpa <player>");
            return true;
        }

        try {
            Player requester = (Player) sender;
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found: " + args[0]);
                return true;
            }

            if (target.equals(requester)) {
                sender.sendMessage("§cYou cannot send a TPA request to yourself.");
                return true;
            }

            new tpa(target, requester).send();
            return true;
        } catch (Exception e) {
            sender.sendMessage("§cAn error occurred while processing the command. Please contact a2thx or el_clappo to report the issue. Thanks!");
            return true;
        }
    }
}
