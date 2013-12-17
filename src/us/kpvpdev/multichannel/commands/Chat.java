package us.kpvpdev.multichannel.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import us.kpvpdev.multichannel.MultiChannel;
import us.kpvpdev.multichannel.objects.ChatChannel;

public class Chat implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String tag, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;

			if(args.length == 0) {
				sender.sendMessage("§cUsage:§f /" + tag + " <toggle, msg>");
			} else if(args.length >= 1) {
				if(MultiChannel.playerChannels.containsKey(sender.getName())) {
					if(args.length == 1 && (args[0].equalsIgnoreCase("toggle") || args[0].equalsIgnoreCase("t"))) {
						if(MultiChannel.chatting.contains(sender.getName())) {
							MultiChannel.chatting.remove(sender.getName());
						} else {
							MultiChannel.chatting.add(sender.getName());
						}

						sender.sendMessage("§cChannel chat§f " + (MultiChannel.chatting.contains(sender.getName()) ? "enabled" : "disabled"));
					} else {
						StringBuilder builder = new StringBuilder();

						for(int i = 0; i < args.length; i++) {
							builder.append(args[i] + " ");
						}

						ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());
						String msg = "§7[§b" + channel.getName() + "§7]§r " + player.getDisplayName() + " §7>§f " + builder.toString();
						channel.broadcast(msg);
						Bukkit.getLogger().info(ChatColor.stripColor(msg));
					}
				} else {
					sender.sendMessage("§cYou're not in a channel!");
				}
			}

			return true;
		}

		return false;
	}

}