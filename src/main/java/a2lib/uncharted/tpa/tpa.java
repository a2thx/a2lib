package a2lib.uncharted.tpa;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class tpa {
    private final Player target;
    private final Player sender;
    private static final Map<UUID, Map<UUID, tpa>> requests = new HashMap<>();

    public tpa(Player target, Player sender) {
        this.target = target;
        this.sender = sender;
    }

    public void accept() {
        removeRequest(target, sender);
        sender.teleport(target);
        sender.sendMessage("§aYour teleport request to §7" + target.getName() + "§a was accepted.");
        target.sendMessage("§aYou accepted §7" + sender.getName() + "§a's teleport request.");
    }

    public void deny() {
        removeRequest(target, sender);
        sender.sendMessage("§cYour teleport request to §7" + target.getName() + "§c was denied.");
        target.sendMessage("§cYou denied §7" + sender.getName() + "§c's teleport request.");
    }

    public void send() {
        requests.computeIfAbsent(target.getUniqueId(), ignored -> new HashMap<>())
                .put(sender.getUniqueId(), this);
        sender.sendMessage("§aTeleport request sent to §7" + target.getName());
        target.sendMessage("§7" + sender.getName() + "§a wants to tp to you");

        TextComponent acceptButton = new TextComponent("[");
        acceptButton.setColor(ChatColor.GRAY);
        acceptButton.addExtra(new TextComponent("✔"));
        acceptButton.getExtra().get(0).setColor(ChatColor.GREEN);
        acceptButton.addExtra(new TextComponent("Accept"));
        acceptButton.getExtra().get(1).setColor(ChatColor.GREEN);
        acceptButton.getExtra().get(1).setBold(true);
        acceptButton.addExtra(new TextComponent("]"));
        acceptButton.getExtra().get(2).setColor(ChatColor.GRAY);
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + sender.getName()));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Accept " + sender.getName() + "'s TPA request").color(ChatColor.GREEN).create()));
        TextComponent spacer = new TextComponent(" or ");
        spacer.setColor(ChatColor.GRAY);
        TextComponent denyButton = new TextComponent("[");
        denyButton.setColor(ChatColor.GRAY);
        denyButton.addExtra(new TextComponent("✖"));
        denyButton.getExtra().get(0).setColor(ChatColor.RED);
        denyButton.addExtra(new TextComponent("Deny"));
        denyButton.getExtra().get(1).setColor(ChatColor.RED);
        denyButton.getExtra().get(1).setBold(true);
        denyButton.addExtra(new TextComponent("]"));
        denyButton.getExtra().get(2).setColor(ChatColor.GRAY);
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpacancel " + sender.getName()));
        denyButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Deny " + sender.getName() + "'s TPA request").color(ChatColor.RED).create()));
        TextComponent suffix = new TextComponent(" the request");
        suffix.setColor(ChatColor.GRAY);
        target.spigot().sendMessage(acceptButton, spacer, denyButton, suffix);
    }

    public static tpa getRequest(Player target, Player sender) {
        Map<UUID, tpa> targetRequests = requests.get(target.getUniqueId());
        if (targetRequests == null) {
            return null;
        }
        return targetRequests.get(sender.getUniqueId());
    }

    private static void removeRequest(Player target, Player sender) {
        Map<UUID, tpa> targetRequests = requests.get(target.getUniqueId());
        if (targetRequests == null) {
            return;
        }

        targetRequests.remove(sender.getUniqueId());
        if (targetRequests.isEmpty()) {
            requests.remove(target.getUniqueId());
        }
    }
}
