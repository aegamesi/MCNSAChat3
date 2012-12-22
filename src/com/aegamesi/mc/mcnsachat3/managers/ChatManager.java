package com.aegamesi.mc.mcnsachat3.managers;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.aegamesi.mc.mcnsachat3.chat.ChatChannel;
import com.aegamesi.mc.mcnsachat3.chat.ChatPlayer;
import com.aegamesi.mc.mcnsachat3.plugin.MCNSAChat3;
import com.aegamesi.mc.mcnsachat3.plugin.PluginUtil;

public class ChatManager {
	public MCNSAChat3 plugin;

	public ChatManager(MCNSAChat3 plugin) {
		this.plugin = plugin;
	}

	public void chat(ChatPlayer player, String line, String channel) {
		ChatChannel chan = ChannelManager.getChannel(PlayerManager.getPlayer(player).channel);
		if (channel == null || channel.length() <= 0)
			channel = chan.name;
		if (MCNSAChat3.permissions.getUser(player.name).has("mcnsachat3.user.cancolor"))
			line = PluginUtil.color(line);
		else
			line = PluginUtil.stripColor(line);

		if (chan.modes.contains(ChatChannel.Mode.RAVE))
			line = PluginUtil.raveColor(line);

		String message = plugin.getConfig().getString("strings.message");
		message = message.replace("%server%", player.server);
		message = message.replace("%channel%", chan.color + channel);
		message = message.replace("%rank%", PluginUtil.formatRank(player.name));
		message = message.replace("%prefix%", MCNSAChat3.permissions.getUser(player.name).getPrefix());
		message = message.replace("%player%", player.name);
		message = message.replace("%message%", line);

		info(player, message, channel, !(player.server.equals(plugin.name)));
	}

	public void action(ChatPlayer player, String line, String channel) {
		ChatChannel chan = ChannelManager.getChannel(PlayerManager.getPlayer(player).channel);
		if (channel == null || channel.length() <= 0)
			channel = chan.name;
		if (MCNSAChat3.permissions.getUser(player.name).has("mcnsachat3.user.cancolor"))
			line = PluginUtil.color(line);
		else
			line = PluginUtil.stripColor(line);

		if (chan.modes.contains(ChatChannel.Mode.RAVE))
			line = PluginUtil.raveColor(line);

		String message = plugin.getConfig().getString("strings.action");
		message = message.replace("%server%", player.server);
		message = message.replace("%channel%", chan.color + channel);
		message = message.replace("%rank%", PluginUtil.formatRank(player.name));
		message = message.replace("%prefix%", MCNSAChat3.permissions.getUser(player.name).getPrefix());
		message = message.replace("%player%", player.name);
		message = message.replace("%message%", line);

		info(player, message, channel, !(player.server.equals(plugin.name)));
	}

	public void info(ChatPlayer player, String line, String channel, boolean net) {
		ChatChannel chan = ChannelManager.getChannel(channel);
		if (chan == null)
			return;
		ArrayList<ChatPlayer> players = PlayerManager.getPlayersListeningToChannel(chan.name);
		for (ChatPlayer p : players) {
			System.out.println(p.name);
			boolean send = player == null;
			if (!send) {
				if (net)
					send = Bukkit.getPlayerExact(p.name) != null && !p.server.equals(player.server);
				else
					send = Bukkit.getPlayerExact(p.name) != null && p.server.equals(player.server);
			}
			if (send)
				PluginUtil.sendLater(p.name, line);
		}
		Bukkit.getConsoleSender().sendMessage(PluginUtil.color(line));
	}
}
