package a2lib.uncharted.banplus;

import a2lib.uncharted.a2lib;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class bancommand implements CommandExecutor, TabCompleter {

    private final tempban tempBanLogic;
    private final tempmute tempMuteLogic;
    private final unban unbanLogic;
    private final unmute unmuteLogic;

    public bancommand(a2lib plugin) {
        this.tempBanLogic = new tempban(plugin);
        this.tempMuteLogic = new tempmute(plugin);
        this.unbanLogic = new unban(plugin);
        this.unmuteLogic = new unmute(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String subCmd;
        String[] effectiveArgs;

        if (command.getName().equalsIgnoreCase("banplus")) {
            if (args.length == 0) {
                sender.sendMessage("§cUsage: /banplus <tempban|tempmute|unban|unmute> ...");
                return true;
            }
            subCmd = args[0].toLowerCase();
            effectiveArgs = Arrays.copyOfRange(args, 1, args.length);
        } else {
            subCmd = command.getName().toLowerCase();
            effectiveArgs = args;
        }

        if (!sender.hasPermission("banplus." + subCmd) && !sender.hasPermission("banplus.admin")) {
            sender.sendMessage("§cYou do not have permission to use this command");
            return true;
        }

        if (subCmd.equals("unban")) {
            if (effectiveArgs.length < 1) {
                sender.sendMessage("§cUsage: /" + label + " <player>");
                return true;
            }
            @SuppressWarnings("deprecation")
            OfflinePlayer target = Bukkit.getOfflinePlayer(effectiveArgs[0]);
            unbanLogic.removeBan(sender, target);
            return true;
        }

        if (subCmd.equals("unmute")) {
            if (effectiveArgs.length < 1) {
                sender.sendMessage("§cUsage: /" + label + " <player>");
                return true;
            }
            @SuppressWarnings("deprecation")
            OfflinePlayer target = Bukkit.getOfflinePlayer(effectiveArgs[0]);
            unmuteLogic.removeMute(sender, target);
            return true;
        }

        if (subCmd.equals("tempban")) {
            if (effectiveArgs.length < 3) {
                sender.sendMessage("§cUsage: /" + label + " <player> <time> <reason>");
                return true;
            }
            @SuppressWarnings("deprecation")
            OfflinePlayer target = Bukkit.getOfflinePlayer(effectiveArgs[0]);
            if (!isValidTarget(target, effectiveArgs[0])) {
                sender.sendMessage("§cPlayer not found");
                return true;
            }
            long duration = parseTime(effectiveArgs[1]);
            if (duration <= 0) {
                sender.sendMessage("§cInvalid time format");
                return true;
            }
            String reason = String.join(" ", Arrays.copyOfRange(effectiveArgs, 2, effectiveArgs.length));
            tempBanLogic.ban(target, duration, reason, sender.getName());
            sender.sendMessage("§aTemporarily banned §f" + getDisplayName(target, effectiveArgs[0]) + "§a for §f"
                    + effectiveArgs[1] + "§a.");
            return true;
        }

        if (subCmd.equals("tempmute")) {
            if (effectiveArgs.length < 3) {
                sender.sendMessage("§cUsage: /" + label + " <player> <time> <reason>");
                return true;
            }
            @SuppressWarnings("deprecation")
            OfflinePlayer target = Bukkit.getOfflinePlayer(effectiveArgs[0]);
            if (!isValidTarget(target, effectiveArgs[0])) {
                sender.sendMessage("§cPlayer not found");
                return true;
            }
            long duration = parseTime(effectiveArgs[1]);
            if (duration <= 0) {
                sender.sendMessage("§cInvalid time format");
                return true;
            }
            String reason = String.join(" ", Arrays.copyOfRange(effectiveArgs, 2, effectiveArgs.length));
            tempMuteLogic.mute(target, duration, reason, sender.getName());
            sender.sendMessage("§aTemporarily muted §f" + getDisplayName(target, effectiveArgs[0]) + "§a for §f"
                    + effectiveArgs[1] + "§a.");
            return true;
        }

        sender.sendMessage("§cUnknown subcommand");
        return true;
    }

    private long parseTime(String timeStr) {
        timeStr = timeStr.toLowerCase();
        long multiplier = 1;
        if (timeStr.endsWith("d")) {
            multiplier = 86400000L;
        } else if (timeStr.endsWith("h")) {
            multiplier = 3600000L;
        } else if (timeStr.endsWith("m")) {
            multiplier = 60000L;
        } else {
            return -1;
        }

        try {
            int value = Integer.parseInt(timeStr.substring(0, timeStr.length() - 1));
            if (value <= 0)
                return -1;
            return value * multiplier;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (!command.getName().equalsIgnoreCase("banplus")) {
                return completions;
            }
            List<String> options = Arrays.asList("tempban", "tempmute", "unban", "unmute");
            for (String str : options) {
                if (str.startsWith(args[0].toLowerCase()))
                    completions.add(str);
            }
            return completions;
        }

        int playerArgIndex = command.getName().equalsIgnoreCase("banplus") ? 2 : 1;
        int timeArgIndex = command.getName().equalsIgnoreCase("banplus") ? 3 : 2;
        String effectiveSubCmd = command.getName().equalsIgnoreCase("banplus")
                ? (args.length > 0 ? args[0].toLowerCase() : "")
                : command.getName().toLowerCase();

        if (args.length == playerArgIndex) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[playerArgIndex - 1].toLowerCase())) {
                    completions.add(p.getName());
                }
            }
            return completions;
        }

        if (args.length == timeArgIndex && (effectiveSubCmd.equals("tempban") || effectiveSubCmd.equals("tempmute"))) {
                List<String> times = Arrays.asList("1m", "5m", "10m", "1h", "2h", "1d");
                for (String t : times) {
                    if (t.startsWith(args[timeArgIndex - 1].toLowerCase()))
                        completions.add(t);
                }
                return completions;
        }
        return completions;
    }

    private boolean isValidTarget(OfflinePlayer target, String inputName) {
        return target != null && (target.hasPlayedBefore() || target.isOnline() || inputName.equalsIgnoreCase(target.getName()));
    }

    private String getDisplayName(OfflinePlayer target, String fallback) {
        return target.getName() != null ? target.getName() : fallback;
    }
}
