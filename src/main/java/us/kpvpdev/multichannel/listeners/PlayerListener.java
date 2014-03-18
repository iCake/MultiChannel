package us.kpvpdev.multichannel.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import us.kpvpdev.multichannel.MultiChannel;
import us.kpvpdev.multichannel.config.Settings;
import us.kpvpdev.multichannel.objects.ChatChannel;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		if(Settings.JOIN_MESSAGE && !MultiChannel.channels.isEmpty()) {
			for(ChatChannel channel : MultiChannel.channels.values()) {
				if(channel.getMembers().contains(player.getName())) {
					MultiChannel.playerChannels.put(player.getName(), channel);
					player.sendMessage("§7You're in the §b" + channel.getName() + " §7channel");

					break;
				}
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();

		if(MultiChannel.playerChannels.containsKey(player.getName())) {
			MultiChannel.playerChannels.remove(player.getName());
		}

		if(MultiChannel.chatting.contains(player.getName())) {
			MultiChannel.chatting.remove(player.getName());
		}

		if(MultiChannel.spying.contains(player.getName())) {
			MultiChannel.spying.remove(player.getName());
		}
	}

	@EventHandler(ignoreCancelled=true)
	public void onKick(PlayerKickEvent e) {
		Player player = e.getPlayer();

		if(MultiChannel.playerChannels.containsKey(player.getName())) {
			MultiChannel.playerChannels.remove(player.getName());
		}

		if(MultiChannel.chatting.contains(player.getName())) {
			MultiChannel.chatting.remove(player.getName());
		}

		if(MultiChannel.spying.contains(player.getName())) {
			MultiChannel.spying.remove(player.getName());
		}
	}

	@EventHandler(ignoreCancelled=true)
	public void onChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();

		if(MultiChannel.playerChannels.containsKey(player.getName()) && MultiChannel.chatting.contains(player.getName())) {
			ChatChannel channel = MultiChannel.playerChannels.get(player.getName());
			channel.sendMessage(player, e.getMessage());

			e.getRecipients().clear();

			if(Settings.LOG_TO_CONSOLE) {
				Bukkit.getLogger().info(ChatColor.stripColor("§7[§b" + channel.getName() + "§7]§r " + player.getDisplayName() + " §7>§f " + e.getMessage()));
			}
		}
	}

}